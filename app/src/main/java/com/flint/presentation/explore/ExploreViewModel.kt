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
        getInitialCollections()
    }

    fun loadNextPage() {
        val currentState: ExploreUiState = (_uiState.value as? UiState.Success)?.data ?: return
        if (!currentState.canLoadMore) return

        _uiState.update { UiState.Success(currentState.copy(isLoadingMore = true)) }

        viewModelScope.launch {
            fetchCollections(
                cursor = currentState.nextCursor,
                currentCollections = currentState.collections
            )
        }
    }

    private fun getInitialCollections() {
        viewModelScope.launch {
            fetchCollections(
                cursor = null,
                currentCollections = persistentListOf()
            )
        }
    }

    private suspend fun fetchCollections(
        cursor: Long?,
        currentCollections: ImmutableList<CollectionsModel.Collection>,
    ) {
        collectionRepository.getCollections(
            cursor = cursor,
            size = PAGE_SIZE
        ).onSuccess { collectionsModel: CollectionsModel ->
            val newData: ImmutableList<CollectionsModel.Collection> =
                (currentCollections + collectionsModel.data).toImmutableList()

            _uiState.update {
                UiState.Success(
                    ExploreUiState(
                        collections = newData,
                        nextCursor = collectionsModel.meta.nextCursor,
                    )
                )
            }
        }.onFailure {

        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
