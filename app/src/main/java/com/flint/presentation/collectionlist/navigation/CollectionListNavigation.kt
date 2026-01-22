package com.flint.presentation.collectionlist.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.core.navigation.model.CollectionListRouteType
import com.flint.presentation.collectionlist.CollectionListRoute

fun NavController.navigateToCollectionList(
    routeType: CollectionListRouteType,
    userId: String?,
    navOptions: NavOptions? = null
) {
    navigate(
        Route.CollectionList(
            userId = userId,
            routeType = routeType
        ),
        navOptions,
    )
}

fun NavGraphBuilder.collectionListNavGraph(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
    navigateToCollectionList: (CollectionListRouteType) -> Unit,
) {
    composable<Route.CollectionList> {
        CollectionListRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateToCollectionDetail = navigateToCollectionDetail,
            navigateToCollectionList = navigateToCollectionList,
        )
    }
}
