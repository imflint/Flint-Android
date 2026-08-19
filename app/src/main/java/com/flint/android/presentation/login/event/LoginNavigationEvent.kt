package com.flint.android.presentation.login.event

sealed interface LoginNavigationEvent {
    data object NavigateToHome : LoginNavigationEvent
    data class NavigateToOnBoarding(val tempToken: String) : LoginNavigationEvent
}