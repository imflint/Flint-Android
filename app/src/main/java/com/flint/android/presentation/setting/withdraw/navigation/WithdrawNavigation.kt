package com.flint.android.presentation.setting.withdraw.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.android.core.navigation.Route
import com.flint.android.presentation.setting.withdraw.WithdrawRoute

fun NavController.navigateToWithdraw(navOptions: NavOptions? = null) {
    navigate(Route.Withdraw, navOptions)
}

fun NavGraphBuilder.withdrawNavGraph(
    navigateUp: () -> Unit,
    navigateToWithdrawComplete: () -> Unit,
) {
    composable<Route.Withdraw> {
        WithdrawRoute(
            navigateUp = navigateUp,
            navigateToWithdrawComplete = navigateToWithdrawComplete,
        )
    }
}
