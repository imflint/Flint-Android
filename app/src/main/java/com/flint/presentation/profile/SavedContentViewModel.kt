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
import com.flint.presentation.profile.uistate.SavedContentUiState.Companion.MIN_REQUIRED_COUNT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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


    //  북마크 토글 (저장 취소) — 내 프로필에서만 동작
    // 저장된 작품이 MIN_REQUIRED_COUNT(5)개일 때는 취소를 막고 안내 모달을 노출한다.
    fun toggleBookmark(contentId: String) {
        if (userId != null) return  // 타유저 프로필에서는 북마크 토글 불가
        val currentCount = uiState.value.totalCount
        Timber.d("toggleBookmark called: contentId=$contentId, currentCount=$currentCount")

        if (currentCount <= MIN_REQUIRED_COUNT) {
            Timber.d("toggleBookmark blocked: count($currentCount) <= MIN($MIN_REQUIRED_COUNT)")
            _uiState.update { it.copy(showBookmarkRestrictionModal = true) }
            return
        }
        viewModelScope.launch {
            Timber.d("toggleBookmark: calling API for contentId=$contentId")
            bookmarkRepository.toggleContentBookmark(contentId)
                .onSuccess { isBookmarked ->
                    Timber.d("toggleBookmark success: isBookmarked=$isBookmarked")
                    loadBookmarkedContents(showLoading = false)
                }
                .onFailure { throwable ->
                    Timber.e(throwable, "toggleBookmark failed: contentId=$contentId")
                }
        }
    }

    // 저장 취소 제한 안내 모달 닫기
    fun dismissBookmarkRestrictionModal() {
        _uiState.update { it.copy(showBookmarkRestrictionModal = false) }
    }
}
