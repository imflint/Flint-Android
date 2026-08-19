package com.flint.android.presentation.collectiondetail.report.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.android.core.navigation.Route
import com.flint.android.presentation.collectiondetail.report.CollectionReportRoute

const val KEY_SHOW_REPORT_SUCCESS_TOAST = "showReportSuccessToast"

fun NavController.navigateToCollectionReport(
    collectionId: String,
    navOptions: NavOptions? = null,
) {
    navigate(Route.CollectionReport(collectionId = collectionId), navOptions)
}

fun NavGraphBuilder.collectionReportNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateUp: () -> Unit,
) {
    composable<Route.CollectionReport> {
        CollectionReportRoute(
            paddingValues = paddingValues,
            navigateUp = navigateUp,
            navigateUpWithSuccess = {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(KEY_SHOW_REPORT_SUCCESS_TOAST, true)
                navController.navigateUp()
            },
        )
    }
}
