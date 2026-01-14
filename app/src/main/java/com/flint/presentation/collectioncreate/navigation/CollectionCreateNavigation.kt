package com.flint.presentation.collectioncreate.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.collectioncreate.AddFilmRoute
import com.flint.presentation.collectioncreate.CollectionCreateRoute

fun NavController.navigateToCollectionCreate(navOptions: NavOptions? = null) {
    navigate(Route.CollectionCreate, navOptions)
}

fun NavController.navigateToAddFilm(navOptions: NavOptions? = null) {
    navigate(Route.AddFilm, navOptions)
}

fun NavGraphBuilder.collectionCreateNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<Route.CollectionCreate> {
        CollectionCreateRoute(
            paddingValues = paddingValues,
            navigateToAddFilm = navController::navigateToAddFilm,
        )
    }

    composable<Route.AddFilm> {
        AddFilmRoute(
            paddingValues = paddingValues,
            navigateToCollectionCreate = navController::navigateToCollectionCreate,
        )
    }
}
