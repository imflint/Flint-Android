package com.flint.presentation.collectiondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.common.extension.updateSuccess
import com.flint.core.common.util.UiState
import com.flint.core.common.util.suspendRunCatching
import com.flint.core.navigation.Route
import com.flint.domain.model.collection.CollectionDetailModel
import com.flint.domain.repository.BookmarkRepository
import com.flint.domain.repository.CollectionRepository
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
            bookmarkRepository.toggleCollectionBookmark(uiState.collectionId)
                .onSuccess { isBookmarked: Boolean ->
                    _uiState.updateSuccess { it.copy(isBookmarked = isBookmarked) }

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
            suspendRunCatching {
                collectionRepository.getCollectionDetail(collectionId)
            }.onSuccess { collectionDetailModel: CollectionDetailModel ->
                _uiState.update {
                    UiState.Success(
                        CollectionDetailUiState(
                            collectionId = collectionDetailModel.collectionId,
                            isBookmarked = collectionDetailModel.isBookmarked,
                        )
                    )
                }
            }.onFailure {
                // TODO: 데이터 불러오지 못한 경우, 다이얼로그 띄우도록 구현
            }
        }
    }
}