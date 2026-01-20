package com.flint.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.UiState
import com.flint.core.common.util.suspendRunCatching
import com.flint.domain.repository.UserRepository
import com.flint.presentation.profile.uistate.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
                val profile = userRepository.getUserProfile(userId = null).getOrThrow()
                val keywords = userRepository.getUserKeywords(userId = null).getOrThrow()

                ProfileUiState(
                    profile = profile,
                    keywords = keywords
                )
            }.onSuccess { combinedState ->
                _uiState.update { UiState.Success(combinedState) }
                getSectionInfo()
            }
        }
    }

    fun getSectionInfo() {
        viewModelScope.launch {
            val currentState = (_uiState.value as? UiState.Success)?.data ?: return@launch

            suspendRunCatching {
                val createdCollectionsDeferred = async {
                    userRepository.getUserCreatedCollections(userId = null).getOrThrow()
                }
                val bookmarkedCollectionsDeferred = async {
                    userRepository.getUserBookmarkedCollections(userId = null).getOrThrow()
                }

                currentState.copy(
                    createCollections = createdCollectionsDeferred.await(),
                    savedCollections = bookmarkedCollectionsDeferred.await(),
                )
            }.onSuccess { updatedState ->
                _uiState.update { UiState.Success(updatedState) }
            }
        }
    }
}
