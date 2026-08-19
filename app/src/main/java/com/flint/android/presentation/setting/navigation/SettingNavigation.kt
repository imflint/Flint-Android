package com.flint.android.presentation.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.android.core.navigation.Route
import com.flint.android.presentation.setting.SettingRoute

fun NavController.navigateToSetting(navOptions: NavOptions? = null) {
    navigate(Route.Setting, navOptions)
}

fun NavGraphBuilder.settingNavGraph(
    navigateUp: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToEditProfile: () -> Unit,
    navigateToWithdraw: () -> Unit,
) {
    composable<Route.Setting> {
        SettingRoute(
            navigateUp = navigateUp,
            navigateToLogin = navigateToLogin,
            navigateToEditProfile = navigateToEditProfile,
            navigateToWithdraw = navigateToWithdraw,
        )
    }
}
