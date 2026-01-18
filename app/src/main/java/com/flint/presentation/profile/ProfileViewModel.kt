package com.flint.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.extension.updateSuccess
import com.flint.core.common.util.UiState
import com.flint.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow<UiState<ProfileUiState>>(
                UiState.Success(ProfileUiState.Fake),
            )
        val uiState: StateFlow<UiState<ProfileUiState>> = _uiState.asStateFlow()

        init {
            loadInitialData()
        }

        private fun loadInitialData() {
            viewModelScope.launch {
                userRepository.getUserKeywords(userId = 800370427074376635).fold( // TODO: 임시 userId
                    onFailure = {
                        Timber.d("onFailure: $it")
                        _uiState.emit(UiState.Failure)
                    },
                    onSuccess = { result ->
                        Timber.d("onSuccess: $result")
                        _uiState.updateSuccess {
                            it.copy(
                                keywords = result.toState(),
                            )
                        }
                    },
                )
            }
        }

        private fun refreshProfile() {
        }
    }
