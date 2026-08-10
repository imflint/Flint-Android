package com.flint.presentation.onboarding.event

sealed interface OnboardingProfileEvent {
    data class ShowNicknameToast(val message: String, val isSuccess: Boolean) : OnboardingProfileEvent
}
