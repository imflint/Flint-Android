package com.flint.presentation.login.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.login.LoginRoute

fun NavController.navigateToLogin(navOptions: NavOptions) {
    navigate(Route.Login, navOptions)
}

fun NavGraphBuilder.loginNavGraph(
    paddingValues: PaddingValues,
    onNavigateToOnBoarding: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    composable<Route.Login> {
        LoginRoute(
            paddingValues = paddingValues,
            navigateToOnBoarding = onNavigateToOnBoarding,
            navigateToHome = onNavigateToHome,
        )
    }
}
