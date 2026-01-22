package com.flint.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.DataStoreKey.USER_NAME
import com.flint.core.common.util.UiState
import com.flint.data.local.PreferencesManager
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.content.BookmarkedContentListModel
import com.flint.domain.repository.CollectionRepository
import com.flint.domain.repository.ContentRepository
import com.flint.domain.repository.HomeRepository
import com.flint.presentation.home.sideeffect.HomeSideEffect
import com.flint.presentation.home.uistate.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val homeRepository: HomeRepository,
    private val contentRepository: ContentRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _userName = preferencesManager.getString(USER_NAME)
    private val _recommendCollectionListLoadState = MutableStateFlow<UiState<CollectionListModel>>(UiState.Loading)
    private val _bookmarkedContentListLoadState = MutableStateFlow<UiState<BookmarkedContentListModel>>(UiState.Loading)
    private val _recentCollectionListLoadState = MutableStateFlow<UiState<CollectionListModel>>(UiState.Loading)

    private val _homeSideEffect = MutableSharedFlow<HomeSideEffect>()
    val homeSideEffect = _homeSideEffect.asSharedFlow()

    val homeUiState: StateFlow<HomeUiState> = combine(
        _userName,
        _recommendCollectionListLoadState,
        _bookmarkedContentListLoadState,
        _recentCollectionListLoadState
    ) { userName, recommendedCollectionList, bookmarkedContentList, recentCollectionList ->
        HomeUiState(
            userName = userName,
            recommendedCollectionListLoadState = recommendedCollectionList,
            bookmarkedContentListLoadState = bookmarkedContentList,
            recentCollectionListLoadState = recentCollectionList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState(
            userName = "",
            recommendedCollectionListLoadState = UiState.Loading,
            bookmarkedContentListLoadState = UiState.Loading,
            recentCollectionListLoadState = UiState.Loading
        )
    )

    fun getRecommendedCollectionList() = viewModelScope.launch {
        homeRepository.getRecommendedCollectionList()
            .onSuccess {
                _recommendCollectionListLoadState.emit(UiState.Success(it))
            }
            .onFailure {
                Timber.e(it.message)
            }
    }

    fun getBookmarkedContentList() = viewModelScope.launch {
        contentRepository.getBookmarkedContentList()
            .onSuccess {
                _bookmarkedContentListLoadState.emit(UiState.Success(it))
            }
            .onFailure {
                Timber.e(it.message)
            }
    }

    fun getRecentCollectionList() = viewModelScope.launch {
        collectionRepository.getRecentCollectionList()
            .onSuccess {
                _recentCollectionListLoadState.emit(UiState.Success(it))
            }
            .onFailure {
                Timber.e(it.message)
            }
    }

    fun getOttListPerContent(contentId: String) = viewModelScope.launch {
        contentRepository.getOttListPerContent(contentId)
            .onSuccess {
                _homeSideEffect.emit(HomeSideEffect.ShowOttListBottomSheet(it))
            }
            .onFailure {
                Timber.e(it.message)
            }
    }
}