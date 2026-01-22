package com.flint.presentation.collectiondetail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.flint.core.navigation.Route
import com.flint.core.navigation.model.CollectionListRouteType
import com.flint.presentation.collectiondetail.CollectionDetailRoute

fun NavController.navigateToCollectionDetail(
    collectionId: String,
    targetImageUrl: String? = null,
    navOptions: NavOptions? = null,
) {
    navigate(
        Route.CollectionDetail(
            collectionId = collectionId,
            targetImageUrl = targetImageUrl,
        ),
        navOptions,
    )
}

fun NavGraphBuilder.collectionDetailNavGraph(
    paddingValues: PaddingValues,
    navigateToCollectionList: (CollectionListRouteType) -> Unit,
    navigateUp: () -> Unit,
    navigateToProfile: (userId: String) -> Unit,
) {
    composable<Route.CollectionDetail> { backStackEntry ->
        val route = backStackEntry.toRoute<Route.CollectionDetail>()
        CollectionDetailRoute(
            paddingValues = paddingValues,
            targetImageUrl = route.targetImageUrl,
            navigateToCollectionList = navigateToCollectionList,
            navigateUp = navigateUp,
            navigateToProfile = navigateToProfile,
        )
    }
}
