package com.flint.presentation.collectiondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.common.util.UiState
import com.flint.core.navigation.Route
import com.flint.domain.model.bookmark.CollectionBookmarkUsersModel
import com.flint.domain.model.collection.CollectionDetailModelNew
import com.flint.domain.model.content.ContentModelNew
import com.flint.domain.repository.BookmarkRepository
import com.flint.domain.repository.CollectionRepository
import com.flint.presentation.collectiondetail.sideeffect.CollectionDetailSideEffect
import com.flint.presentation.collectiondetail.uistate.CollectionDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bookmarkRepository: BookmarkRepository,
    private val collectionRepository: CollectionRepository,
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

    private var lastCollectionBookmarkToggleTime: Long = 0L
    private val lastContentBookmarkToggleTime: MutableMap<String, Long> = mutableMapOf()
    private val throttleDelayMs: Long = 2000L

    fun toggleCollectionBookmark() {
        val uiState: CollectionDetailUiState = (_uiState.value as? UiState.Success)?.data ?: return
        
        val currentTime: Long = System.currentTimeMillis()
        if (currentTime - lastCollectionBookmarkToggleTime < throttleDelayMs) return
        lastCollectionBookmarkToggleTime = currentTime

        val previousBookmarkState: Boolean = uiState.collectionDetail.isBookmarked

        updateCollectionBookmarkState(!previousBookmarkState)

        viewModelScope.launch {
            bookmarkRepository.toggleCollectionBookmark(uiState.collectionDetail.id)
                .onSuccess { isBookmarked: Boolean ->
                    updateCollectionBookmarkState(isBookmarked)
                    getCollectionBookmarkUsers()
                    _sideEffect.emit(
                        CollectionDetailSideEffect.ToggleCollectionBookmarkSuccess(isBookmarked)
                    )
                }
                .onFailure {
                    updateCollectionBookmarkState(previousBookmarkState)
                    _sideEffect.emit(CollectionDetailSideEffect.ToggleCollectionBookmarkFailure)
                }
        }
    }

    fun toggleContentBookmark(contentId: String) {
        val uiState: CollectionDetailUiState = (_uiState.value as? UiState.Success)?.data ?: return

        val currentTime: Long = System.currentTimeMillis()
        val lastToggleTime: Long = lastContentBookmarkToggleTime[contentId] ?: 0L
        if (currentTime - lastToggleTime < throttleDelayMs) return
        lastContentBookmarkToggleTime[contentId] = currentTime

        val targetContent: ContentModelNew =
            uiState.collectionDetail.contents.find { it.id == contentId } ?: return
        val previousBookmarkState: Boolean = targetContent.isBookmarked
        val previousBookmarkCount: Int = targetContent.bookmarkCount

        updateContentBookmarkState(
            contentId = contentId,
            isBookmarked = !previousBookmarkState,
            bookmarkCount =
                if (!previousBookmarkState) previousBookmarkCount + 1
                else (previousBookmarkCount - 1).coerceAtLeast(0)
        )

        viewModelScope.launch {
            bookmarkRepository.toggleContentBookmark(contentId)
                .onSuccess { isBookmarked: Boolean ->
                    updateContentIsBookmarkedOnly(
                        contentId = contentId,
                        isBookmarked = isBookmarked
                    )
                    _sideEffect.emit(
                        CollectionDetailSideEffect.ToggleContentBookmarkSuccess(isBookmarked)
                    )
                }
                .onFailure {
                    updateContentBookmarkState(
                        contentId = contentId,
                        isBookmarked = previousBookmarkState,
                        bookmarkCount = previousBookmarkCount
                    )
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

                UiState.Success(
                    CollectionDetailUiState(
                        collectionDetail = collectionDetail.await().getOrThrow(),
                        collectionBookmarkUsers = collectionBookmarkUsers.await().getOrThrow()
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