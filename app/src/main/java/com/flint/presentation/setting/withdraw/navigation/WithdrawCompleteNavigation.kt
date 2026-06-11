package com.flint.presentation.setting.withdraw.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.setting.withdraw.WithdrawCompleteRoute

fun NavController.navigateToWithdrawComplete(navOptions: NavOptions? = null) {
    navigate(Route.WithdrawComplete, navOptions)
}

fun NavGraphBuilder.withdrawCompleteNavGraph(
    navigateToLogin: () -> Unit,
) {
    composable<Route.WithdrawComplete> {
        WithdrawCompleteRoute(
            navigateToLogin = navigateToLogin,
        )
    }
}
