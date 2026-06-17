package com.flint.presentation.collectiondetail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.flint.core.navigation.Route
import com.flint.core.navigation.model.CollectionListRouteType
import com.flint.presentation.collectioncreate.navigation.navigateToCollectionEdit
import com.flint.presentation.collectiondetail.CollectionDetailRoute

fun NavController.navigateToCollectionDetail(
    collectionId: String,
    targetImageUrl: String? = null,
    showEditSuccessToast: Boolean = false,
    navOptions: NavOptions? = null,
) {
    navigate(
        Route.CollectionDetail(
            collectionId = collectionId,
            targetImageUrl = targetImageUrl,
            showEditSuccessToast = showEditSuccessToast,
        ),
        navOptions,
    )
}

const val KEY_SHOW_DELETE_SUCCESS_TOAST = "showDeleteSuccessToast"

fun NavGraphBuilder.collectionDetailNavGraph(
    paddingValues: PaddingValues,
    navigateToCollectionList: (CollectionListRouteType) -> Unit,
    navigateUp: () -> Unit,
    navigateToProfile: (userId: String) -> Unit,
    navController: NavController,
) {
    composable<Route.CollectionDetail> { backStackEntry ->
        val route = backStackEntry.toRoute<Route.CollectionDetail>()
        CollectionDetailRoute(
            paddingValues = paddingValues,
            targetImageUrl = route.targetImageUrl,
            showEditSuccessToast = route.showEditSuccessToast,
            navigateToCollectionList = navigateToCollectionList,
            navigateUp = navigateUp,
            navigateUpWithDeleteSuccess = {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(KEY_SHOW_DELETE_SUCCESS_TOAST, true)
                navController.navigateUp()
            },
            navigateToProfile = navigateToProfile,
            navigateToCollectionEdit = { collectionId ->
                navController.navigateToCollectionEdit(collectionId)
            },
        )
    }
}
