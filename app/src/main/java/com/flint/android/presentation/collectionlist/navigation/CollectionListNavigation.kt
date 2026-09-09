package com.flint.android.presentation.collectionlist.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.android.core.analytics.CollectionSource
import com.flint.android.core.navigation.Route
import com.flint.android.core.navigation.model.CollectionListRouteType
import com.flint.android.presentation.collectionlist.CollectionListRoute

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
    navigateToCollectionDetail: (collectionId: String, source: CollectionSource) -> Unit,
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
