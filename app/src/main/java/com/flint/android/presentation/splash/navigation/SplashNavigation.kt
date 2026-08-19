package com.flint.android.presentation.splash.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.android.core.navigation.Route
import com.flint.android.presentation.splash.SplashRoute

fun NavController.navigateToSplash(navOptions: NavOptions? = null) {
    navigate(Route.Splash, navOptions)
}

fun NavGraphBuilder.splashNavGraph(
    paddingValues: PaddingValues,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
) {
    composable<Route.Splash> {
        SplashRoute(
            paddingValues = paddingValues,
            navigateToLogin = navigateToLogin,
            navigateToHome = navigateToHome,
        )
    }
}
