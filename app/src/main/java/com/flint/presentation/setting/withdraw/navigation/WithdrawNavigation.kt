package com.flint.presentation.setting.withdraw.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.setting.withdraw.WithdrawRoute

fun NavController.navigateToWithdraw(navOptions: NavOptions? = null) {
    navigate(Route.Withdraw, navOptions)
}

fun NavGraphBuilder.withdrawNavGraph(
    navigateUp: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    composable<Route.Withdraw> {
        WithdrawRoute(
            navigateUp = navigateUp,
            navigateToLogin = navigateToLogin,
        )
    }
}
