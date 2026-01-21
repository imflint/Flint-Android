package com.flint.presentation.onboarding

data class OnboardingProfileUiState(
    val nickname: String = "",
    val isValid: Boolean = false,
    val isNicknameAvailable: Boolean? = null,
) {
    companion object {
        const val MAX_LENGTH = 8
        const val MIN_LENGTH = 2
    }

    //다믐단게 활성화
    val canProceed: Boolean
        get() = isValid && isNicknameAvailable == true
}