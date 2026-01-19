package com.flint.presentation.collectiondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.extension.updateSuccess
import com.flint.core.common.util.UiState
import com.flint.domain.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDetailViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<CollectionDetailUiState>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CollectionDetailUiState>> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<CollectionDetailEvent> = MutableSharedFlow()
    val event: SharedFlow<CollectionDetailEvent> = _event.asSharedFlow()

    fun toggleCollectionBookmark() {
        val uiState: CollectionDetailUiState = (_uiState.value as? UiState.Success)?.data ?: return

        viewModelScope.launch {
            bookmarkRepository.toggleCollectionBookmark(uiState.collectionId)
                .fold(
                    onSuccess = { isBookmarked: Boolean ->
                        _uiState.updateSuccess { it.copy(isBookmarked = isBookmarked) }

                        _event.emit(
                            CollectionDetailEvent.ToggleCollectionBookmarkSuccess(
                                isBookmarked
                            )
                        )
                    },
                    onFailure = {
                        _event.emit(CollectionDetailEvent.ToggleCollectionBookmarkFailure)
                    },
                )
        }
    }
}