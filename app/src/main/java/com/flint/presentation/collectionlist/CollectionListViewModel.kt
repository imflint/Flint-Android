package com.flint.presentation.collectionlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.common.util.UiState
import com.flint.core.navigation.Route
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.repository.BookmarkRepository
import com.flint.domain.repository.CollectionRepository
import com.flint.domain.repository.UserRepository
import com.flint.core.navigation.model.CollectionListRouteType
import com.flint.presentation.collectionlist.sideeffect.CollectionListSideEffect
import com.flint.presentation.collectionlist.uistate.CollectionListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val collectionRepository: CollectionRepository,
    private val bookmarkRepository: BookmarkRepository,
) : ViewModel() {

    val routeReceiveData = savedStateHandle.toRoute<Route.CollectionList>()

    private val _uiState = MutableStateFlow<CollectionListUiState>(CollectionListUiState())
    val uiState: StateFlow<CollectionListUiState> = _uiState

    private val _sideEffect: MutableSharedFlow<CollectionListSideEffect> = MutableSharedFlow()
    val sideEffect: SharedFlow<CollectionListSideEffect> = _sideEffect.asSharedFlow()

    init {
        getCollectionList()
    }

    private fun getCollectionList() {
        val userId = routeReceiveData.userId

        viewModelScope.launch {
            _uiState.update { it.copy(collectionList = UiState.Loading) }

            when (routeReceiveData.routeType) {
                CollectionListRouteType.CREATED -> userRepository.getUserCreatedCollections(userId = userId)
                CollectionListRouteType.SAVED -> userRepository.getUserBookmarkedCollections(userId = userId)
                CollectionListRouteType.RECENT -> collectionRepository.getRecentCollectionList()
            }.onSuccess { result ->
                _uiState.update { it.copy(collectionList = UiState.Success(result)) }
            }.onFailure {
                _uiState.update { it.copy(collectionList = UiState.Failure) }
            }
        }
    }

    fun toggleCollectionBookmark(collectionId: String) {
        viewModelScope.launch {
            bookmarkRepository.toggleCollectionBookmark(collectionId)
                .onSuccess { isBookmarked: Boolean ->
                    _uiState.update { currentState: CollectionListUiState ->
                        val collectionList = currentState.collectionList
                        if (collectionList !is UiState.Success) return@update currentState

                        val updatedCollections = collectionList.data.collections.map { collection ->
                            if (collection.id == collectionId) {
                                collection.copy(
                                    isBookmarked = isBookmarked,
                                    bookmarkCount = if (isBookmarked) {
                                        collection.bookmarkCount + 1
                                    } else {
                                        collection.bookmarkCount - 1
                                    }
                                )
                            } else {
                                collection
                            }
                        }.toImmutableList()

                        currentState.copy(
                            collectionList = UiState.Success(
                                CollectionListModel(collections = updatedCollections)
                            )
                        )
                    }

                    _sideEffect.emit(
                        CollectionListSideEffect.ToggleCollectionBookmarkSuccess(isBookmarked)
                    )
                }
                .onFailure {
                    _sideEffect.emit(CollectionListSideEffect.ToggleCollectionBookmarkFailure)
                }
        }
    }
}
