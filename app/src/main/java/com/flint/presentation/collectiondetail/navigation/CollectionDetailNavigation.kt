package com.flint.presentation.collectiondetail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.collectiondetail.CollectionDetailRoute

fun NavController.navigateToCollectionDetail(
    collectionId: String,
    navOptions: NavOptions? = null,
) {
    navigate(Route.CollectionDetail(collectionId = collectionId), navOptions)
}

fun NavGraphBuilder.collectionDetailNavGraph(
    paddingValues: PaddingValues,
    navigateToCollectionList: () -> Unit,
    navigateUp: () -> Unit,
) {
    composable<Route.CollectionDetail> {
        CollectionDetailRoute(
            paddingValues = paddingValues,
            navigateToCollectionList = navigateToCollectionList,
            navigateUp = navigateUp,
        )
    }
}
