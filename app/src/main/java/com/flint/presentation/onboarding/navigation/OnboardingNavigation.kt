package com.flint.presentation.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.flint.core.common.extension.sharedViewModel
import com.flint.core.navigation.Route
import com.flint.presentation.onboarding.OnboardingContentRoute
import com.flint.presentation.onboarding.OnboardingDoneRoute
import com.flint.presentation.onboarding.OnboardingOttRoute
import com.flint.presentation.onboarding.OnboardingProfileRoute
import com.flint.presentation.onboarding.OnboardingViewModel

fun NavController.navigateToOnboarding(tempToken: String) {
    navigate(Route.OnboardingGraph(tempToken))  // OnboardingGraph로 네비게이트
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
    navController: NavHostController,
) {
    navigation<Route.OnboardingGraph>(
        startDestination = Route.OnboardingProfile::class,
    ) {
        composable<Route.OnboardingProfile> { backStackEntry ->
            val sharedViewModel = backStackEntry.sharedViewModel<OnboardingViewModel>(navController)

            OnboardingProfileRoute(
                paddingValues = paddingValues,
                navigateUp = navController::navigateUp,
                navigateToOnboardingContent = navController::navigateToOnboardingContent,
                viewModel = sharedViewModel,
                )
        }

        composable<Route.OnboardingContent> { backStackEntry ->
            val sharedViewModel = backStackEntry.sharedViewModel<OnboardingViewModel>(navController)

            OnboardingContentRoute(
                paddingValues = paddingValues,
                navigateUp = navController::navigateUp,
                navigateToOnboardingOtt = navController::navigateToOnboardingOtt,
                viewModel = sharedViewModel,
                )
        }

        composable<Route.OnboardingOtt> { backStackEntry ->
            val sharedViewModel = backStackEntry.sharedViewModel<OnboardingViewModel>(navController)

            OnboardingOttRoute(
                paddingValues = paddingValues,
                navigateUp = navController::navigateUp,
                navigateToDone = navController::navigateToOnboardingDone,
                viewModel = sharedViewModel,
                )
        }

        composable<Route.OnboardingDone> { backStackEntry ->
            val sharedViewModel = backStackEntry.sharedViewModel<OnboardingViewModel>(navController)

            OnboardingDoneRoute(
                paddingValues = paddingValues,
                navigateUp = navController::navigateUp,
                navigateToHome = onNavigateToHome,
                viewModel = sharedViewModel,
                )
        }
    }
}