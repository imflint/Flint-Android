package com.flint.presentation.collectiondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.common.util.UiState
import com.flint.core.navigation.Route
import com.flint.domain.model.bookmark.CollectionBookmarkUsersModel
import com.flint.domain.model.collection.CollectionDetailModelNew
import com.flint.domain.repository.BookmarkRepository
import com.flint.domain.repository.CollectionRepository
import com.flint.presentation.collectiondetail.uistate.CollectionDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
        getCollectionDetail(collectionId)
    }

    private val _uiState: MutableStateFlow<UiState<CollectionDetailUiState>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CollectionDetailUiState>> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<CollectionDetailEvent> = MutableSharedFlow()
    val event: SharedFlow<CollectionDetailEvent> = _event.asSharedFlow()

    fun toggleCollectionBookmark() {
        val uiState: CollectionDetailUiState = (_uiState.value as? UiState.Success)?.data ?: return

        viewModelScope.launch {
            bookmarkRepository.toggleCollectionBookmark(uiState.collectionDetail.id)
                .onSuccess { isBookmarked: Boolean ->
                    _uiState.update { uiState: UiState<CollectionDetailUiState> ->
                        if (uiState !is UiState.Success) return@update uiState

                        uiState.copy(
                            data = uiState.data.copy(
                                collectionDetail = uiState.data.collectionDetail.copy(
                                    isBookmarked = isBookmarked
                                )
                            )
                        )
                    }

                    _event.emit(
                        CollectionDetailEvent.ToggleCollectionBookmarkSuccess(isBookmarked)
                    )
                }
                .onFailure {
                    _event.emit(CollectionDetailEvent.ToggleCollectionBookmarkFailure)
                }
        }
    }

    private fun getCollectionDetail(collectionId: String) {
        viewModelScope.launch {
            runCatching {
                val collectionDetail: CollectionDetailModelNew =
                    collectionRepository.getCollectionDetail(collectionId).getOrThrow()
                val collectionBookmarkUsers: CollectionBookmarkUsersModel =
                    bookmarkRepository.getCollectionBookmarkUsers(collectionId).getOrThrow()

                UiState.Success(
                    CollectionDetailUiState(
                        collectionDetail = collectionDetail,
                        collectionBookmarkUsers = collectionBookmarkUsers
                    )
                )
            }.onSuccess { newUiState: UiState.Success<CollectionDetailUiState> ->
                _uiState.update { newUiState }
            }.onFailure {
                // TODO: 데이터 불러오지 못한 경우, 다이얼로그 띄우도록 구현
            }
        }
    }
}