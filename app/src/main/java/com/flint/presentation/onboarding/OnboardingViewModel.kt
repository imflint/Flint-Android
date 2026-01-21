package com.flint.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
@Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingProfileUiState())
    val uiState: StateFlow<OnboardingProfileUiState> = _uiState.asStateFlow()

    // ---------- onboarding profile ----------
    fun updateNickname(nickname: String) {
        if (nickname.length <= OnboardingProfileUiState.MAX_LENGTH) {
            _uiState.update { currentState ->
                currentState.copy(
                    nickname = nickname,
                    isValid = nickname.length >= OnboardingProfileUiState.MIN_LENGTH,
                    isNicknameAvailable = null,
                )
            }
        }
    }

    fun checkNicknameDuplication() {
        val currentNickname = _uiState.value.nickname

        viewModelScope.launch {
            userRepository.checkNickname(currentNickname).onSuccess { result ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isNicknameAvailable = result.isAvailable,
                        )
                    }
                }
        }
    }

    // ---------- onboarding content ----------

    // ---------- onboarding ott ----------

    // ---------- onboarding done ----------
}