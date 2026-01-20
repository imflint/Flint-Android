package com.flint.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.UiState
import com.flint.core.common.util.suspendRunCatching
import com.flint.domain.model.collection.CollectionModel
import com.flint.domain.model.content.ContentModel
import com.flint.domain.repository.UserRepository
import com.flint.presentation.profile.uistate.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ProfileUiState>>(
        UiState.Empty
    )
    val uiState: StateFlow<UiState<ProfileUiState>> = _uiState.asStateFlow()

    fun getProfile() {
        viewModelScope.launch {
            suspendRunCatching {
                val profileDeferred = async { userRepository.getUserProfile(userId = null) }
                val keywordsDeferred = async { userRepository.getUserKeywords(userId = null) }

                val profileResult = profileDeferred.await()
                val keywordsResult = keywordsDeferred.await()

                Pair(profileResult.getOrThrow(), keywordsResult.getOrThrow())

                ProfileUiState(
                    profile = profileResult.getOrThrow(),
                    keywords = keywordsResult.getOrThrow().toImmutableList(),
                    //TODO: 임시
                    savedContent = ContentModel.FakeList,
                    createCollections = CollectionModel.FakeList,
                    savedCollections = CollectionModel.FakeList,
                )
            }.onSuccess { combinedState ->
                _uiState.update { UiState.Success(combinedState) }
            }
        }
    }
}
