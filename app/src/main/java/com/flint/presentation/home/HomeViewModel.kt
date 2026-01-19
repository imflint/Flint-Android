package com.flint.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.datastore.PreferencesManager
import com.flint.domain.repository.ContentRepository
import com.flint.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val homeRepository: HomeRepository,
    private val contentRepository: ContentRepository
) : ViewModel() {



    fun getRecommendedCollectionList() = viewModelScope.launch {
        homeRepository.getRecommendedCollectionList()
            .onSuccess {

            }
    }

    fun getBookmarkedContentList() = viewModelScope.launch {
        contentRepository.getBookmarkedContentList()
            .onSuccess {

            }
    }

}