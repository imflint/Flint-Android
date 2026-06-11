package com.flint.presentation.setting.editprofile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.setting.editprofile.EditProfileRoute

fun NavController.navigateToEditProfile(navOptions: NavOptions? = null) {
    navigate(Route.EditProfile, navOptions)
}

fun NavGraphBuilder.editProfileNavGraph(
    navigateUp: () -> Unit,
) {
    composable<Route.EditProfile> {
        EditProfileRoute(
            navigateUp = navigateUp,
        )
    }
}
