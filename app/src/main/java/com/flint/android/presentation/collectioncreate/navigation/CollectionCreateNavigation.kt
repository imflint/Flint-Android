package com.flint.android.presentation.collectioncreate.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.flint.android.core.common.extension.sharedViewModel
import com.flint.android.core.navigation.Route
import com.flint.android.presentation.collectioncreate.AddContentRoute
import com.flint.android.presentation.collectioncreate.CollectionCreateRoute
import com.flint.android.presentation.collectioncreate.CollectionCreateViewModel
import com.flint.android.presentation.collectiondetail.navigation.navigateToCollectionDetail

fun NavController.navigateToCollectionCreate(
    navOptions: NavOptions? = null
) {
    navigate(Route.CollectionCreate, navOptions)
}

fun NavController.navigateToCollectionEdit(
    collectionId: String,
    navOptions: NavOptions? = null,
) {
    navigate(Route.CollectionCreateGraph(collectionId = collectionId), navOptions)
}

fun NavController.navigateToAddContent(
    navOptions: NavOptions? = null
) {
    navigate(Route.AddContent, navOptions)
}

fun NavGraphBuilder.collectionCreateNavGraph(
    paddingValues: PaddingValues,
    navController: NavHostController,
) {
    navigation< Route.CollectionCreateGraph>(
        startDestination = Route.CollectionCreate,
    ){
        composable<Route.CollectionCreate> { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<CollectionCreateViewModel>(navController)

            CollectionCreateRoute(
                paddingValues = paddingValues,
                navigateToAddContent = navController::navigateToAddContent,
                navigateUp = navController::navigateUp,
                navigateToCollectionDetail = { collectionId ->
                    if (viewModel.isEditMode) {
                        navController.navigateToCollectionDetail(
                            collectionId = collectionId,
                            showEditSuccessToast = true,
                            navOptions = navOptions {
                                popUpTo<Route.CollectionDetail> { inclusive = true }
                            }
                        )
                    } else {
                        navController.popBackStack<Route.CollectionCreateGraph>(inclusive = true)
                        navController.navigateToCollectionDetail(collectionId = collectionId)
                    }
                },
                viewModel = viewModel
            )
        }

        composable<Route.AddContent> {backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<CollectionCreateViewModel>(navController)

            AddContentRoute(
                paddingValues = paddingValues,
                navigateUp = navController::navigateUp,
                navigateToCollectionCreate = navController::navigateUp,
                viewModel = viewModel
            )
        }
    }
}
