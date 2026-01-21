package com.flint.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.UiState
import com.flint.domain.model.collection.CollectionsModel
import com.flint.domain.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<CollectionsModel>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CollectionsModel>> = _uiState.asStateFlow()

    init {
        getCollections()
    }

    private fun getCollections() {
        val uiState: CollectionsModel = (_uiState.value as? UiState.Success)?.data ?: return
        viewModelScope.launch {
            collectionRepository.getCollections(cursor = 0, page = 100)
                .onSuccess { collectionsModel: CollectionsModel ->
                    _uiState.value = UiState.Success(collectionsModel)
                }
                .onFailure {

                }
        }
    }
}