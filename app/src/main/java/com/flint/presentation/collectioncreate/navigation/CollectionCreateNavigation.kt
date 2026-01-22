package com.flint.presentation.collectioncreate.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.flint.core.common.extension.sharedViewModel
import com.flint.core.navigation.Route
import com.flint.presentation.collectioncreate.AddContentRoute
import com.flint.presentation.collectioncreate.CollectionCreateRoute
import com.flint.presentation.collectioncreate.CollectionCreateViewModel
import com.flint.presentation.collectiondetail.navigation.navigateToCollectionDetail

fun NavController.navigateToCollectionCreate(
    navOptions: NavOptions? = null
) {
    navigate(Route.CollectionCreate, navOptions)
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
                navigateToCollectionDetail = {
                    navController.navigateToCollectionDetail(it,
                        navOptions {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        })
                },
                navController = navController,
                viewModel = viewModel
            )
        }

        composable<Route.AddContent> {backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<CollectionCreateViewModel>(navController)

            AddContentRoute(
                paddingValues = paddingValues,
                navigateUp = navController::navigateUp,
                navigateToCollectionCreate = navController::navigateToCollectionCreate,
                viewModel = viewModel
            )
        }
    }
}
