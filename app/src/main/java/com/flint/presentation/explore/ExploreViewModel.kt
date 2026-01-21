package com.flint.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.UiState
import com.flint.domain.model.collection.CollectionsModel
import com.flint.domain.repository.CollectionRepository
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

    private val _uiState: MutableStateFlow<UiState<CollectionsModel>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CollectionsModel>> = _uiState.asStateFlow()

    private var currentCursor: Int = 1
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private val pageSize: Int = 10

    init {
        getCollections()
    }

    private fun getCollections() {
        if (isLoading || isLastPage) return

        isLoading = true
        viewModelScope.launch {
            collectionRepository.getCollections(cursor = currentCursor, size = pageSize)
                .onSuccess { collectionsModel: CollectionsModel ->
                    val currentData: ImmutableList<CollectionsModel.Collection> =
                        (_uiState.value as? UiState.Success)?.data?.data ?: persistentListOf()
                    val newData: ImmutableList<CollectionsModel.Collection> =
                        (currentData + collectionsModel.data).toImmutableList()

                    _uiState.update {
                        UiState.Success(
                            collectionsModel.copy(data = newData)
                        )
                    }

                    currentCursor = collectionsModel.meta.nextCursor.toIntOrNull() ?: 0
                    isLastPage = collectionsModel.meta.nextCursor.isEmpty() ||
                            collectionsModel.data.isEmpty()
                    isLoading = false
                }
                .onFailure {
                    isLoading = false
                }
        }
    }

    fun loadNextPage() {
        getCollections()
    }
}