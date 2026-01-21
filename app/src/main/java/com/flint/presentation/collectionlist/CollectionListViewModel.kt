package com.flint.presentation.collectionlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.UiState
import com.flint.domain.repository.UserRepository
import com.flint.presentation.collectionlist.uistate.CollectionListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionListViewModel @Inject constructor(
    val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<CollectionListUiState>>(
        UiState.Loading
    )
    val uiState: MutableStateFlow<UiState<CollectionListUiState>> = _uiState

    init {
        getCollectionList()
    }

    fun getCollectionList() {
        viewModelScope.launch {
            userRepository.getUserCreatedCollections(userId = null)
                .onSuccess { myInfo ->
                    _uiState.emit(
                        UiState.Success(
                            CollectionListUiState(
                                collectionList = myInfo,
                            ),
                        ),
                    )
                }.onFailure {}
        }
    }
}
