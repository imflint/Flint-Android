package com.flint.android.presentation.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.android.core.navigation.Route
import com.flint.android.presentation.login.LoginRoute

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(Route.Login, navOptions)
}

fun NavGraphBuilder.loginNavGraph(
    navigateToOnBoarding: (tempToken: String) -> Unit,
    navigateToHome: () -> Unit,
) {
    composable<Route.Login> {
        LoginRoute(
            navigateToOnBoarding = navigateToOnBoarding,
            navigateToHome = navigateToHome,
        )
    }
}
