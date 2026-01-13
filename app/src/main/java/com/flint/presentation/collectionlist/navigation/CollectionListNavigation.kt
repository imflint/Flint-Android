package com.flint.presentation.collectionlist.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.collectionlist.CollectionListRoute

fun NavController.navigateToCollectionList(navOptions: NavOptions? = null) {
    navigate(Route.CollectionList, navOptions)
}

fun NavGraphBuilder.collectionListNavGraph(
    paddingValues: PaddingValues,
    navigateToCollectionDetail: () -> Unit,
) {
    composable<Route.CollectionList> {
        CollectionListRoute(
            paddingValues = paddingValues,
            navigateToCollectionDetail = navigateToCollectionDetail,
        )
    }
}
