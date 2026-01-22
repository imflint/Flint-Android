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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    private val _uiState = MutableStateFlow<CollectionListUiState>(CollectionListUiState())
    val uiState: StateFlow<CollectionListUiState> = _uiState

    private val _sideEffect: MutableSharedFlow<CollectionListSideEffect> = MutableSharedFlow()
    val sideEffect: SharedFlow<CollectionListSideEffect> = _sideEffect.asSharedFlow()

    private val debounceDelayMs: Long = 500L
    private val collectionBookmarkDebounceJobs: MutableMap<String, Job> = mutableMapOf()
    private val initialCollectionBookmarkStates: MutableMap<String, Boolean> = mutableMapOf()

    init {
        val routeReceiveData = savedStateHandle.toRoute<Route.CollectionList>()
        setAppBarTitle(routeReceiveData.routeType.title)
        getCollectionList(routeReceiveData)
    }

    private fun setAppBarTitle(title: String) {
        _uiState.update { it.copy(appbarTitle = title) }
    }

    private fun getCollectionList(data: Route.CollectionList) {
        viewModelScope.launch {
            _uiState.update { it.copy(collectionList = UiState.Loading) }

            when (data.routeType) {
                CollectionListRouteType.CREATED -> userRepository.getUserCreatedCollections(userId = data.userId)
                CollectionListRouteType.SAVED -> userRepository.getUserBookmarkedCollections(userId = data.userId)
                CollectionListRouteType.RECENT -> collectionRepository.getRecentCollectionList() // 홈
            }.onSuccess { result ->
                _uiState.update { it.copy(collectionList = UiState.Success(result)) }
            }.onFailure {
                _uiState.update { it.copy(collectionList = UiState.Failure) }
            }
        }
    }

    fun toggleCollectionBookmark(collectionId: String) {
        val collectionList = (_uiState.value.collectionList as? UiState.Success)?.data ?: return
        val targetCollection = collectionList.collections.find { it.id == collectionId } ?: return

        if (initialCollectionBookmarkStates[collectionId] == null) {
            initialCollectionBookmarkStates[collectionId] = targetCollection.isBookmarked
        }

        val newBookmarkState = !targetCollection.isBookmarked
        val initialBookmarkCount = targetCollection.bookmarkCount
        val adjustedBookmarkCount =
            if (newBookmarkState) initialBookmarkCount + 1
            else (initialBookmarkCount - 1).coerceAtLeast(0)

        updateCollectionBookmarkState(
            collectionId = collectionId,
            isBookmarked = newBookmarkState,
            bookmarkCount = adjustedBookmarkCount
        )

        collectionBookmarkDebounceJobs[collectionId]?.cancel()
        collectionBookmarkDebounceJobs[collectionId] = viewModelScope.launch {
            delay(debounceDelayMs)

            val currentCollection = (_uiState.value.collectionList as? UiState.Success)?.data
                ?.collections?.find { it.id == collectionId } ?: return@launch

            val initialState = initialCollectionBookmarkStates[collectionId] ?: return@launch

            if (currentCollection.isBookmarked != initialState) {
                bookmarkRepository.toggleCollectionBookmark(collectionId)
                    .onSuccess { isBookmarked: Boolean ->
                        updateCollectionIsBookmarkedOnly(
                            collectionId = collectionId,
                            isBookmarked = isBookmarked
                        )
                        _sideEffect.emit(
                            CollectionListSideEffect.ToggleCollectionBookmarkSuccess(isBookmarked)
                        )
                    }
                    .onFailure {
                        val fallbackCollection = (_uiState.value.collectionList as? UiState.Success)
                            ?.data?.collections?.find { it.id == collectionId } ?: return@onFailure

                        val rollbackCount =
                            if (initialState) fallbackCollection.bookmarkCount + 1
                            else (fallbackCollection.bookmarkCount - 1).coerceAtLeast(0)

                        updateCollectionBookmarkState(
                            collectionId = collectionId,
                            isBookmarked = initialState,
                            bookmarkCount = rollbackCount
                        )
                        _sideEffect.emit(CollectionListSideEffect.ToggleCollectionBookmarkFailure)
                    }
            }

            initialCollectionBookmarkStates.remove(collectionId)
            collectionBookmarkDebounceJobs.remove(collectionId)
        }
    }

    private fun updateCollectionBookmarkState(
        collectionId: String,
        isBookmarked: Boolean,
        bookmarkCount: Int,
    ) {
        _uiState.update { currentState: CollectionListUiState ->
            val collectionList = currentState.collectionList
            if (collectionList !is UiState.Success) return@update currentState

            val updatedCollections = collectionList.data.collections.map { collection ->
                if (collection.id == collectionId) {
                    collection.copy(
                        isBookmarked = isBookmarked,
                        bookmarkCount = bookmarkCount
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
    }

    private fun updateCollectionIsBookmarkedOnly(collectionId: String, isBookmarked: Boolean) {
        _uiState.update { currentState: CollectionListUiState ->
            val collectionList = currentState.collectionList
            if (collectionList !is UiState.Success) return@update currentState

            val updatedCollections = collectionList.data.collections.map { collection ->
                if (collection.id == collectionId) {
                    collection.copy(isBookmarked = isBookmarked)
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
    }
}
