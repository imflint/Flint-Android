package com.flint.presentation.collectiondetail.report.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.Route
import com.flint.presentation.collectiondetail.report.CollectionReportRoute

fun NavController.navigateToCollectionReport(
    collectionId: String,
    navOptions: NavOptions? = null,
) {
    navigate(Route.CollectionReport(collectionId = collectionId), navOptions)
}

fun NavGraphBuilder.collectionReportNavGraph(
    navigateUp: () -> Unit,
) {
    composable<Route.CollectionReport> {
        CollectionReportRoute(
            navigateUp = navigateUp,
        )
    }
}
