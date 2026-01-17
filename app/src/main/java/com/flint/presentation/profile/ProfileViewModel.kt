package com.flint.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.extension.updateSuccess
import com.flint.core.common.util.UiState
import com.flint.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow<UiState<ProfileUiState>>(
                UiState.Success(
                    ProfileUiState.Empty,
                ),
            )
        val uiState: StateFlow<UiState<ProfileUiState>> = _uiState.asStateFlow()

        init {
            loadInitialData()
        }

        private fun loadInitialData() {
            viewModelScope.launch {
                userRepository.getUserKeywords(userId = 800370427074376600).fold(
                    onFailure = {
                        _uiState.emit(UiState.Failure)
                    },
                    onSuccess = { result ->
//                        _uiState.value = UiState.Success(ProfileUiState.Empty) //TODO: 임시 Empty 상태 처리

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
