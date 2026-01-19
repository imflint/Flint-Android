package com.flint.presentation.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.onboarding.OnboardingDoneRoute
import com.flint.presentation.onboarding.OnboardingContentRoute
import com.flint.presentation.onboarding.OnboardingOttRoute
import com.flint.presentation.onboarding.OnboardingProfileRoute

fun NavController.navigateToOnboarding(tempToken: String) {
    navigate(Route.OnboardingProfile(tempToken))
}

fun NavController.navigateToOnboardingContent() {
    navigate(Route.OnboardingContent)
}

fun NavController.navigateToOnboardingOtt() {
    navigate(Route.OnboardingOtt)
}

fun NavController.navigateToOnboardingDone() {
    navigate(Route.OnboardingDone)
}

fun NavGraphBuilder.onBoardingNavGraph(
    paddingValues: PaddingValues,
    onNavigateToHome: () -> Unit,
    navController: NavController,
) {
    composable<Route.OnboardingProfile> {
        OnboardingProfileRoute(
            paddingValues = paddingValues,
            navigateUp = navController::navigateUp,
            navigateToOnboardingContent = navController::navigateToOnboardingContent,
        )
    }

    composable<Route.OnboardingContent> {
        OnboardingContentRoute(
            paddingValues = paddingValues,
            navigateUp = navController::navigateUp,
            navigateToOnboardingOtt = navController::navigateToOnboardingOtt,
        )
    }

    composable<Route.OnboardingOtt> {
        OnboardingOttRoute(
            paddingValues = paddingValues,
            navigateUp = navController::navigateUp,
            navigateToDone = navController::navigateToOnboardingDone,
        )
    }

    composable<Route.OnboardingDone> {
        OnboardingDoneRoute(
            paddingValues = paddingValues,
            navigateUp = navController::navigateUp,
            navigateToHome = onNavigateToHome,
        )
    }
}
