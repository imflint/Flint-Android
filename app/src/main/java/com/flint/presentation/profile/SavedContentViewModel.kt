package com.flint.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.common.util.UiState
import com.flint.core.navigation.Route
import com.flint.domain.repository.BookmarkRepository
import com.flint.domain.repository.ContentRepository
import com.flint.domain.repository.UserRepository
import com.flint.presentation.profile.uistate.SavedContentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SavedContentViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    private val userRepository: UserRepository,
    private val bookmarkRepository: BookmarkRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val userId: String? = savedStateHandle.toRoute<Route.SavedContentList>().userId

    private val _uiState = MutableStateFlow(SavedContentUiState())
    val uiState: StateFlow<SavedContentUiState> = _uiState.asStateFlow()

    init {
        loadBookmarkedContents()
    }


    // 사용자 저장한 작품 목록 호출
    fun loadBookmarkedContents(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                _uiState.update { it.copy(contents = UiState.Loading) }
            }

            userRepository.getUserBookmarkedContents(userId)
                .onSuccess { list ->
                    _uiState.update {
                        it.copy(
                            contents = if (list.contents.isEmpty()) {
                                UiState.Empty
                            } else {
                                UiState.Success(list)
                            },
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update { it.copy(contents = UiState.Failure) }
                    Timber.e(throwable)
                }
        }
    }


    fun updateSearchKeyword(keyword: String) {
        _uiState.update { it.copy(searchKeyword = keyword) }
    }

    // 검색어 초기화
    fun clearSearchKeyword() {
        _uiState.update { it.copy(searchKeyword = "") }
    }


    // 북마크 토글
    // - 내 프로필: 북마크 취소 시 목록에서 제거 (MIN_REQUIRED_COUNT 제한 있음)
    // - 타 유저 프로필: isBookmarked 필드만 토글 (목록은 상대방 저장 목록이므로 제거 X)
    fun toggleBookmark(contentId: String) {
        viewModelScope.launch {
            Timber.d("toggleBookmark: contentId=$contentId, userId=$userId")
            bookmarkRepository.toggleContentBookmark(contentId)
                .onSuccess { isBookmarked ->
                    Timber.d("toggleBookmark success: isBookmarked=$isBookmarked")
                    if (userId == null) {
                        // 내 프로필: 북마크 취소 시 목록에서 제거
                        _uiState.update { state ->
                            val currentList = (state.contents as? UiState.Success)?.data
                                ?: return@update state
                            val updated = if (isBookmarked) {
                                currentList
                            } else {
                                val filtered = currentList.contents
                                    .filter { it.id != contentId }
                                    .toPersistentList()
                                currentList.copy(contents = filtered, totalCount = filtered.size)
                            }
                            state.copy(
                                contents = if (updated.contents.isEmpty()) UiState.Empty
                                else UiState.Success(updated)
                            )
                        }
                    } else {
                        // 타 유저 프로필: isBookmarked 필드만 업데이트 (목록에서 제거 X)
                        _uiState.update { state ->
                            val currentList = (state.contents as? UiState.Success)?.data
                                ?: return@update state
                            val updated = currentList.copy(
                                contents = currentList.contents
                                    .map { if (it.id == contentId) it.copy(isBookmarked = isBookmarked) else it }
                                    .toPersistentList()
                            )
                            state.copy(contents = UiState.Success(updated))
                        }
                    }
                }
                .onFailure { throwable ->
                    val isMinLimitError = (throwable as? HttpException)
                        ?.response()?.errorBody()?.string()
                        ?.let { body ->
                            runCatching {
                                Json.parseToJsonElement(body)
                                    .jsonObject["errorCode"]
                                    ?.jsonPrimitive?.content == "BOOKMARK.CONTENT_MIN_LIMIT"
                            }.getOrDefault(false)
                        } ?: false

                    if (isMinLimitError) {
                        _uiState.update { it.copy(showBookmarkRestrictionModal = true) }
                    } else {
                        Timber.e(throwable, "toggleBookmark failed: contentId=$contentId")
                    }
                }
        }
    }

    // 저장 취소 제한 안내 모달 닫기
    fun dismissBookmarkRestrictionModal() {
        _uiState.update { it.copy(showBookmarkRestrictionModal = false) }
    }
}
