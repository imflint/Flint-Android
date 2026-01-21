package com.flint.presentation.collectionlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.common.util.UiState
import com.flint.core.navigation.Route
import com.flint.domain.repository.UserRepository
import com.flint.domain.type.CollectionListRouteType
import com.flint.presentation.collectionlist.uistate.CollectionListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val userRepository: UserRepository
) : ViewModel() {

    val routeType = savedStateHandle.toRoute<Route.CollectionList>().routeType

    private val _uiState = MutableStateFlow<CollectionListUiState>(CollectionListUiState())
    val uiState: MutableStateFlow<CollectionListUiState> = _uiState

    init {
        getCollectionList()
    }

    fun getCollectionList() {
        //TODO: routeType에 따라 조회 데이터 변경 필요
        viewModelScope.launch {
            when (routeType) {
                CollectionListRouteType.CREATED -> {
                    userRepository.getUserCreatedCollections(userId = null)
                        .onSuccess { result ->
                            _uiState.update {
                                it.copy(collectionList = UiState.Success(result))
                            }
                        }.onFailure { error ->
                            _uiState.update {
                                it.copy(collectionList = UiState.Failure)
                            }
                        }
                }

                CollectionListRouteType.SAVED -> {

                }
                CollectionListRouteType.RECENT -> {

                }
            }
        }
    }
}
