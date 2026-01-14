package com.flint.presentation.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.onboarding.OnboardingFilmRoute
import com.flint.presentation.onboarding.OnboardingOttRoute
import com.flint.presentation.onboarding.OnboardingProfileRoute

fun NavController.navigateToOnboarding() {
    navigate(Route.OnboardingProfile)
}

fun NavController.navigateToOnboardingFilm() {
    navigate(Route.OnboardingFilm)
}

fun NavController.navigateToOnboardingOtt() {
    navigate(Route.OnboardingOtt)
}

fun NavGraphBuilder.onBoardingNavGraph(
    paddingValues: PaddingValues,
    onNavigateToHome: () -> Unit,
    navController: NavController,
) {
    composable<Route.OnboardingProfile> {
        OnboardingProfileRoute(
            paddingValues = paddingValues,
            navigateToOnboardingFilm = navController::navigateToOnboardingFilm,
        )
    }

    composable<Route.OnboardingFilm> {
        OnboardingFilmRoute(
            paddingValues = paddingValues,
            navigateToOnboardingOtt = navController::navigateToOnboardingOtt,
        )
    }

    composable<Route.OnboardingOtt> {
        OnboardingOttRoute(
            paddingValues = paddingValues,
            navigateToHome = onNavigateToHome,
        )
    }
}
