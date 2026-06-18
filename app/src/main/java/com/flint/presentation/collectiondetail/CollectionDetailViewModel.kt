package com.flint.presentation.collectiondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.common.util.DataStoreKey.USER_ID
import com.flint.core.common.util.UiState
import com.flint.core.navigation.Route
import com.flint.data.local.PreferencesManager
import com.flint.domain.model.bookmark.CollectionBookmarkUsersModel
import com.flint.domain.model.collection.CollectionDetailModelNew
import com.flint.domain.model.content.ContentModelNew
import com.flint.domain.model.bookmark.BookmarkException
import com.flint.domain.repository.BookmarkRepository
import com.flint.domain.repository.CollectionRepository
import com.flint.presentation.collectiondetail.sideeffect.CollectionDetailSideEffect
import com.flint.presentation.collectiondetail.uistate.CollectionDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bookmarkRepository: BookmarkRepository,
    private val collectionRepository: CollectionRepository,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {
    init {
        val collectionId: String = savedStateHandle.toRoute<Route.CollectionDetail>().collectionId
        getCollectionDetailAndBookmarkUsers(collectionId)
    }

    private val _uiState: MutableStateFlow<UiState<CollectionDetailUiState>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CollectionDetailUiState>> = _uiState.asStateFlow()

    private val _sideEffect: MutableSharedFlow<CollectionDetailSideEffect> = MutableSharedFlow()
    val sideEffect: SharedFlow<CollectionDetailSideEffect> = _sideEffect.asSharedFlow()

    private val debounceDelayMs: Long = 500L

    private var collectionBookmarkDebounceJob: Job? = null
    private var initialCollectionBookmarkState: Boolean? = null

    private val contentBookmarkDebounceJobs: MutableMap<String, Job> = mutableMapOf()
    private val initialContentBookmarkStates: MutableMap<String, Boolean> = mutableMapOf()

    fun toggleCollectionBookmark() {
        val uiState: CollectionDetailUiState = (_uiState.value as? UiState.Success)?.data ?: return

        if (initialCollectionBookmarkState == null) {
            initialCollectionBookmarkState = uiState.collectionDetail.isBookmarked
        }

        updateCollectionBookmarkState(!uiState.collectionDetail.isBookmarked)

        collectionBookmarkDebounceJob?.cancel()
        collectionBookmarkDebounceJob = viewModelScope.launch {
            delay(debounceDelayMs)

            val currentState: Boolean =
                (_uiState.value as? UiState.Success)?.data?.collectionDetail?.isBookmarked ?: return@launch

            if (currentState != initialCollectionBookmarkState) {
                bookmarkRepository.toggleCollectionBookmark(uiState.collectionDetail.id)
                    .onSuccess { isBookmarked: Boolean ->
                        updateCollectionBookmarkState(isBookmarked)
                        getCollectionBookmarkUsers()
                        _sideEffect.emit(
                            CollectionDetailSideEffect.ToggleCollectionBookmarkSuccess(isBookmarked)
                        )
                    }
                    .onFailure {
                        initialCollectionBookmarkState?.let { updateCollectionBookmarkState(it) }
                        _sideEffect.emit(CollectionDetailSideEffect.ToggleCollectionBookmarkFailure)
                    }
            }

            initialCollectionBookmarkState = null
        }
    }

    fun toggleContentBookmark(contentId: String) {
        val uiState: CollectionDetailUiState = (_uiState.value as? UiState.Success)?.data ?: return

        val targetContent: ContentModelNew =
            uiState.collectionDetail.contents.find { it.id == contentId } ?: return

        if (initialContentBookmarkStates[contentId] == null) {
            initialContentBookmarkStates[contentId] = targetContent.isBookmarked
        }

        val newBookmarkState: Boolean = !targetContent.isBookmarked
        val initialBookmarkCount: Int = targetContent.bookmarkCount

        // 북마크 취소
        if (!newBookmarkState) {
            contentBookmarkDebounceJobs[contentId]?.cancel()
            contentBookmarkDebounceJobs.remove(contentId)
            val initialState = initialContentBookmarkStates.remove(contentId)

            if (initialState == false) {
                updateContentBookmarkState(
                    contentId = contentId,
                    isBookmarked = false,
                    bookmarkCount = (initialBookmarkCount - 1).coerceAtLeast(0),
                )
                return
            }

            viewModelScope.launch {
                bookmarkRepository.toggleContentBookmark(contentId)
                    .onSuccess { isBookmarked: Boolean ->
                        updateContentBookmarkState(
                            contentId = contentId,
                            isBookmarked = isBookmarked,
                            bookmarkCount = (initialBookmarkCount - 1).coerceAtLeast(0),
                        )
                        _sideEffect.emit(CollectionDetailSideEffect.ToggleContentBookmarkSuccess(isBookmarked))
                    }
                    .onFailure { throwable ->
                        if (throwable is BookmarkException.ContentMinLimitExceeded) {
                            _sideEffect.emit(CollectionDetailSideEffect.ToggleContentBookmarkMinLimitExceeded)
                        }
                    }
            }
            return
        }

        // 북마크 추가
        val adjustedBookmarkCount: Int = initialBookmarkCount + 1
        updateContentBookmarkState(
            contentId = contentId,
            isBookmarked = newBookmarkState,
            bookmarkCount = adjustedBookmarkCount,
        )

        contentBookmarkDebounceJobs[contentId]?.cancel()
        contentBookmarkDebounceJobs[contentId] = viewModelScope.launch {
            delay(debounceDelayMs)

            val currentContent: ContentModelNew =
                (_uiState.value as? UiState.Success)?.data?.collectionDetail?.contents
                    ?.find { it.id == contentId } ?: return@launch

            val initialState: Boolean = initialContentBookmarkStates[contentId] ?: return@launch

            if (currentContent.isBookmarked != initialState) {
                bookmarkRepository.toggleContentBookmark(contentId)
                    .onSuccess { isBookmarked: Boolean ->
                        updateContentIsBookmarkedOnly(
                            contentId = contentId,
                            isBookmarked = isBookmarked,
                        )
                        _sideEffect.emit(
                            CollectionDetailSideEffect.ToggleContentBookmarkSuccess(isBookmarked)
                        )
                    }
                    .onFailure {
                        val fallbackContent: ContentModelNew =
                            (_uiState.value as? UiState.Success)?.data?.collectionDetail?.contents
                                ?.find { it.id == contentId } ?: return@onFailure

                        updateContentBookmarkState(
                            contentId = contentId,
                            isBookmarked = initialState,
                            bookmarkCount = (fallbackContent.bookmarkCount - 1).coerceAtLeast(0),
                        )
                    }
            }

            initialContentBookmarkStates.remove(contentId)
            contentBookmarkDebounceJobs.remove(contentId)
        }
    }

    fun deleteCollection() {
        val collectionId = (_uiState.value as? UiState.Success)?.data?.collectionDetail?.id ?: return
        viewModelScope.launch {
            collectionRepository.deleteCollection(collectionId)
                .onSuccess {
                    Timber.d("DELETE SUCCESS collectionId=$collectionId")
                    _sideEffect.emit(CollectionDetailSideEffect.DeleteCollectionSuccess)
                }
                .onFailure {
                    Timber.e(it, "DELETE FAILURE collectionId=$collectionId")
                    _sideEffect.emit(CollectionDetailSideEffect.DeleteCollectionFailure)
                }
        }
    }

    fun spoil(contentId: String) {
        _uiState.update { uiState: UiState<CollectionDetailUiState> ->
            if (uiState !is UiState.Success) return@update uiState

            uiState.copy(
                data = uiState.data.copy(
                    collectionDetail = uiState.data.collectionDetail.copy(
                        contents = uiState.data.collectionDetail.contents.map { content: ContentModelNew ->
                            if (content.id == contentId) {
                                content.copy(
                                    isSpoiler = false
                                )
                            } else content
                        }.toImmutableList()
                    )
                )
            )
        }
    }

    private fun getCollectionBookmarkUsers() {
        val uiState: CollectionDetailUiState = (_uiState.value as? UiState.Success)?.data ?: return

        viewModelScope.launch {
            bookmarkRepository.getCollectionBookmarkUsers(uiState.collectionDetail.id)
                .onSuccess { collectionBookmarkUsers: CollectionBookmarkUsersModel ->
                    _uiState.update { uiState: UiState<CollectionDetailUiState> ->
                        if (uiState !is UiState.Success) return@update uiState

                        uiState.copy(
                            data = uiState.data.copy(
                                collectionBookmarkUsers = collectionBookmarkUsers
                            )
                        )
                    }
                }.onFailure {
                    // TODO: 데이터 불러오지 못한 경우, 다이얼로그 띄우도록 구현
                }
        }
    }

    private fun getCollectionDetailAndBookmarkUsers(collectionId: String) {
        viewModelScope.launch {
            runCatching {
                val collectionDetail: Deferred<Result<CollectionDetailModelNew>> =
                    async { collectionRepository.getCollectionDetail(collectionId) }
                val collectionBookmarkUsers: Deferred<Result<CollectionBookmarkUsersModel>> =
                    async { bookmarkRepository.getCollectionBookmarkUsers(collectionId) }
                val myUserId: String =
                    runCatching { preferencesManager.getString(USER_ID).first() }
                        .getOrElse {
                            Timber.w(it, "USER_ID 조회 실패. isMine=false로 처리")
                            ""
                        }

                val collectionDetailResult: CollectionDetailModelNew = collectionDetail.await().getOrThrow()

                UiState.Success(
                    CollectionDetailUiState(
                        collectionDetail = collectionDetailResult,
                        collectionBookmarkUsers = collectionBookmarkUsers.await().getOrThrow(),
                        isMine = myUserId.isNotBlank() && collectionDetailResult.author.id == myUserId,
                    )
                )
            }.onSuccess { newUiState: UiState.Success<CollectionDetailUiState> ->
                _uiState.update { newUiState }
            }.onFailure {
                // TODO: 데이터 불러오지 못한 경우, 다이얼로그 띄우도록 구현
            }
        }
    }

    private fun updateCollectionBookmarkState(isBookmarked: Boolean) {
        _uiState.update { currentUiState: UiState<CollectionDetailUiState> ->
            if (currentUiState !is UiState.Success) return@update currentUiState

            currentUiState.copy(
                data = currentUiState.data.copy(
                    collectionDetail = currentUiState.data.collectionDetail.copy(
                        isBookmarked = isBookmarked
                    )
                )
            )
        }
    }

    private fun updateContentBookmarkState(
        contentId: String,
        isBookmarked: Boolean,
        bookmarkCount: Int,
    ) {
        _uiState.update { currentUiState: UiState<CollectionDetailUiState> ->
            if (currentUiState !is UiState.Success) return@update currentUiState

            currentUiState.copy(
                data = currentUiState.data.copy(
                    collectionDetail = currentUiState.data.collectionDetail.copy(
                        contents = currentUiState.data.collectionDetail.contents.map { content: ContentModelNew ->
                            if (content.id == contentId) {
                                content.copy(
                                    isBookmarked = isBookmarked,
                                    bookmarkCount = bookmarkCount
                                )
                            } else {
                                content
                            }
                        }.toImmutableList()
                    )
                )
            )
        }
    }

    private fun updateContentIsBookmarkedOnly(contentId: String, isBookmarked: Boolean) {
        _uiState.update { currentUiState: UiState<CollectionDetailUiState> ->
            if (currentUiState !is UiState.Success) return@update currentUiState

            currentUiState.copy(
                data = currentUiState.data.copy(
                    collectionDetail = currentUiState.data.collectionDetail.copy(
                        contents = currentUiState.data.collectionDetail.contents.map { content: ContentModelNew ->
                            if (content.id == contentId) {
                                content.copy(isBookmarked = isBookmarked)
                            } else {
                                content
                            }
                        }.toImmutableList()
                    )
                )
            )
        }
    }
}
