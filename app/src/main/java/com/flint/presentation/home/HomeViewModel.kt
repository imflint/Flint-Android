package com.flint.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.datastore.PreferencesManager
import com.flint.core.common.util.UiState
import com.flint.domain.model.collection.CollectionModel
import com.flint.domain.model.content.ContentModel
import com.flint.domain.repository.CollectionRepository
import com.flint.domain.repository.ContentRepository
import com.flint.domain.repository.HomeRepository
import com.flint.presentation.home.uiState.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val homeRepository: HomeRepository,
    private val contentRepository: ContentRepository,
    private val collectionRepository: CollectionRepository
) : ViewModel() {

    private val _recommendCollectionListLoadState = MutableStateFlow<UiState<List<CollectionModel>>>(UiState.Loading)
    private val _bookmarkedContentListLoadState = MutableStateFlow<UiState<List<ContentModel>>>(UiState.Loading)
    private val _recentCollectionListLoadState = MutableStateFlow<UiState<List<CollectionModel>>>(UiState.Loading)

    val homeUiState: StateFlow<HomeUiState> = combine(
        _recommendCollectionListLoadState,
        _bookmarkedContentListLoadState,
        _recentCollectionListLoadState
    ) { recommendedCollectionList, bookmarkedContentList, recentCollectionList ->
        HomeUiState(
            recommendedCollectionListLoadState = recommendedCollectionList,
            bookmarkedContentListLoadState = bookmarkedContentList,
            recentCollectionListLoadState = recentCollectionList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState(
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
                Log.d("Logd", it.message.toString())
            }
    }

    fun getBookmarkedContentList() = viewModelScope.launch {
        contentRepository.getBookmarkedContentList()
            .onSuccess {
                _bookmarkedContentListLoadState.emit(UiState.Success(it))
            }
    }

    fun getRecentCollectionList() = viewModelScope.launch {
        collectionRepository.getRecentCollectionList()
            .onSuccess {
                _recentCollectionListLoadState.emit(UiState.Success(it))
            }
            .onFailure {
                Log.d("Logd", it.message.toString())
            }
    }
}