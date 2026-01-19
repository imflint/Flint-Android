package com.flint.presentation.profile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.MainTabRoute
import com.flint.presentation.profile.ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    navigate(MainTabRoute.Profile, navOptions)
}

fun NavGraphBuilder.profileNavGraph(
    paddingValues: PaddingValues,
    navigateToCollectionList: () -> Unit,
    navigateToSavedContentList: () -> Unit,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
) {
    composable<MainTabRoute.Profile> {
        ProfileRoute(
            paddingValues = paddingValues,
            navigateToCollectionList = navigateToCollectionList,
            navigateToSavedContentList = navigateToSavedContentList,
            navigateToCollectionDetail = navigateToCollectionDetail,
        )
    }
}
