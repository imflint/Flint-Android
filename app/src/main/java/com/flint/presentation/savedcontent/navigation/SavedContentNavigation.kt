package com.flint.presentation.savedcontent.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.savedcontent.SavedContentListRoute

fun NavController.navigateToSavedContentList(navOptions: NavOptions? = null) {
    navigate(Route.SavedContentList, navOptions)
}

fun NavGraphBuilder.savedContentListNavGraph(paddingValues: PaddingValues) {
    composable<Route.SavedContentList> {
        SavedContentListRoute(
            paddingValues = paddingValues,
        )
    }
}
