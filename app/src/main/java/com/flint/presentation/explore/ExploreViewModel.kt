package com.flint.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.UiState
import com.flint.domain.model.collection.CollectionsModel
import com.flint.domain.repository.CollectionRepository
import com.flint.presentation.explore.uistate.ExploreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<ExploreUiState>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<ExploreUiState>> = _uiState.asStateFlow()

    init {
        getCollections()
    }

    private fun getCollections() {
        val currentState: ExploreUiState =
            (_uiState.value as? UiState.Success)?.data ?: ExploreUiState(persistentListOf())
        if (currentState.isLoadingMore || currentState.isLastPage) return

        if (currentState.collections.isEmpty()) {
            _uiState.update { UiState.Loading }
        } else {
            _uiState.update { UiState.Success(currentState.copy(isLoadingMore = true)) }
        }

        viewModelScope.launch {
            collectionRepository.getCollections(
                cursor = currentState.nextCursor,
                size = PAGE_SIZE
            ).onSuccess { collectionsModel: CollectionsModel ->
                val newData: ImmutableList<CollectionsModel.Collection> =
                    (currentState.collections + collectionsModel.data).toImmutableList()

                _uiState.update {
                    UiState.Success(
                        ExploreUiState(
                            collections = newData,
                            nextCursor = collectionsModel.meta.nextCursor,
                            isLastPage = collectionsModel.meta.nextCursor == null,
                            isLoadingMore = false
                        )
                    )
                }
            }.onFailure {

            }
        }
    }

    fun loadNextPage() {
        getCollections()
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}