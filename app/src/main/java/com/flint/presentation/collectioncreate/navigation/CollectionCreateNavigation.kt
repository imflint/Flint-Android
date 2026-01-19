package com.flint.presentation.collectioncreate.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.collectioncreate.AddContentRoute
import com.flint.presentation.collectioncreate.CollectionCreateRoute

fun NavController.navigateToCollectionCreate(navOptions: NavOptions? = null) {
    navigate(Route.CollectionCreate, navOptions)
}

fun NavController.navigateToAddContent(navOptions: NavOptions? = null) {
    navigate(Route.AddContent, navOptions)
}

fun NavGraphBuilder.collectionCreateNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<Route.CollectionCreate> {
        CollectionCreateRoute(
            paddingValues = paddingValues,
            navigateToAddContent = navController::navigateToAddContent,
        )
    }

    composable<Route.AddContent> {
        AddContentRoute(
            paddingValues = paddingValues,
            navigateToCollectionCreate = navController::navigateToCollectionCreate,
        )
    }
}
