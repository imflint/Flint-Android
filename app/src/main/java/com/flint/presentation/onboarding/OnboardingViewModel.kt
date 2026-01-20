package com.flint.presentation.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
@Inject
constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingProfileUiState())
    val uiState: StateFlow<OnboardingProfileUiState> = _uiState.asStateFlow()

    // ---------- onboarding profile ----------
    fun updateNickname(nickname: String) {
        if (nickname.length <= OnboardingProfileUiState.MAX_LENGTH) {
            _uiState.update { currentState ->
                currentState.copy(
                    nickname = nickname,
                    isValid = nickname.isNotEmpty()
                )
            }
        }
    }

    // ---------- onboarding contnet ----------

    // ---------- onboarding ott ----------

    // ---------- onboarding done ----------
}