package com.flint.presentation.onboarding

data class OnboardingProfileUiState(
    val nickname: String = "",
    val isValid: Boolean = false,
) {
    companion object {
        const val MAX_LENGTH = 10
    }
}