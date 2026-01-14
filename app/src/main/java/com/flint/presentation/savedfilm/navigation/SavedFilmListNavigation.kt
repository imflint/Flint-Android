package com.flint.presentation.savedfilm.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.savedfilm.SavedFilmListRoute

fun NavController.navigateToSavedFilmList(navOptions: NavOptions? = null) {
    navigate(Route.SavedFilmList, navOptions)
}

fun NavGraphBuilder.savedFilmListNavGraph(paddingValues: PaddingValues) {
    composable<Route.SavedFilmList> {
        SavedFilmListRoute(
            paddingValues = paddingValues,
        )
    }
}
