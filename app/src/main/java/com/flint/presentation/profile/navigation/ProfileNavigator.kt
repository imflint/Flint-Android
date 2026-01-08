package com.flint.presentation.profile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.MainTabRoute
import com.flint.presentation.profile.ProfileRoute
import kotlinx.serialization.Serializable

@Serializable
data object Profile : MainTabRoute

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    navigate(MainTabRoute.Profile, navOptions)
}

fun NavGraphBuilder.profileNavGraph(
    paddingValues: PaddingValues
){
    composable<Profile> {
        ProfileRoute(paddingValues)
    }
}