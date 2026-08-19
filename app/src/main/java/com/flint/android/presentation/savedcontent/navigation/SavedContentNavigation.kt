package com.flint.android.presentation.savedcontent.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.android.core.navigation.Route
import com.flint.android.presentation.savedcontent.SavedContentListRoute

fun NavController.navigateToSavedContentList(userId: String? = null, navOptions: NavOptions? = null) {
    navigate(Route.SavedContentList(userId = userId), navOptions)
}

fun NavGraphBuilder.savedContentListNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    composable<Route.SavedContentList> {
        SavedContentListRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}
