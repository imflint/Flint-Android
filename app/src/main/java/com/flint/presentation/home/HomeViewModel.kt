package com.flint.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.DataStoreKey.USER_NAME
import com.flint.core.common.util.UiState
import com.flint.data.local.PreferencesManager
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.repository.HomeRepository
import com.flint.domain.repository.UserRepository
import com.flint.presentation.home.uistate.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val homeRepository: HomeRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _userName = preferencesManager.getString(USER_NAME)
    private val _recommendCollectionListLoadState = MutableStateFlow<UiState<CollectionListModel>>(UiState.Loading)
    private val _bookmarkedCollectionListLoadState = MutableStateFlow<UiState<CollectionListModel>>(UiState.Loading)
    private val _popularCollectionListLoadState = MutableStateFlow<UiState<CollectionListModel>>(UiState.Loading)

    val homeUiState: StateFlow<HomeUiState> = combine(
        _userName,
        _recommendCollectionListLoadState,
        _bookmarkedCollectionListLoadState,
        _popularCollectionListLoadState
    ) { userName, recommendedCollectionList, bookmarkedCollectionList, popularCollectionList ->
        HomeUiState(
            userName = userName,
            recommendedCollectionListLoadState = recommendedCollectionList,
            bookmarkedCollectionListLoadState = bookmarkedCollectionList,
            popularCollectionListLoadState = popularCollectionList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState(
            userName = "",
            recommendedCollectionListLoadState = UiState.Loading,
            bookmarkedCollectionListLoadState = UiState.Loading,
            popularCollectionListLoadState = UiState.Loading
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

    fun getBookmarkedCollectionList() = viewModelScope.launch {
        userRepository.getUserBookmarkedCollections(userId = null)
            .onSuccess {
                _bookmarkedCollectionListLoadState.emit(UiState.Success(it))
            }
            .onFailure {
                Timber.e(it.message)
            }
    }

    fun getPopularCollectionList() = viewModelScope.launch {
        homeRepository.getPopularCollectionList()
            .onSuccess {
                _popularCollectionListLoadState.emit(UiState.Success(it))
            }
            .onFailure {
                Timber.e(it.message)
            }
    }
}