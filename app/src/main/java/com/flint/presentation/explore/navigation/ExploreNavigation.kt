package com.flint.presentation.explore.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.MainTabRoute
import com.flint.presentation.explore.ExploreRoute

fun NavController.navigateToExplore(navOptions: NavOptions? = null) {
    navigate(MainTabRoute.Explore, navOptions)
}

fun NavGraphBuilder.exploreNavGraph(
    paddingValues: PaddingValues,
    navigateToCollectionDetail: (collectionId: String, imageUrl: String) -> Unit,
    navigateToCollectionCreate: () -> Unit,
) {
    composable<MainTabRoute.Explore> {
        ExploreRoute(
            paddingValues = paddingValues,
            navigateToCollectionDetail = navigateToCollectionDetail,
            navigateToCollectionCreate = navigateToCollectionCreate,
        )
    }
}
