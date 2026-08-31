package com.flint.android.presentation.collectiondetail.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.flint.android.core.analytics.CollectionSource
import com.flint.android.core.analytics.FlintEvent
import com.flint.android.core.analytics.TrackScreenView
import com.flint.android.core.navigation.Route
import com.flint.android.core.navigation.model.CollectionListRouteType
import com.flint.android.presentation.collectioncreate.navigation.navigateToCollectionEdit
import com.flint.android.presentation.collectiondetail.CollectionDetailRoute
import com.flint.android.presentation.collectiondetail.report.navigation.navigateToCollectionReport

fun NavController.navigateToCollectionDetail(
    collectionId: String,
    source: CollectionSource,
    targetImageUrl: String? = null,
    showEditSuccessToast: Boolean = false,
    navOptions: NavOptions? = null,
) {
    navigate(
        Route.CollectionDetail(
            collectionId = collectionId,
            targetImageUrl = targetImageUrl,
            showEditSuccessToast = showEditSuccessToast,
            source = source.value,
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
    navigateToCollectionReport: (collectionId: String) -> Unit,
    navController: NavController,
) {
    composable<Route.CollectionDetail> { backStackEntry ->
        val route = backStackEntry.toRoute<Route.CollectionDetail>()

        // 진입 경로를 알 수 없으면 잘못된 source 로 집계하느니 이벤트를 생략한다.
        CollectionSource.from(route.source)?.let { source ->
            TrackScreenView(FlintEvent.ViewCollection(route.collectionId, source))
        }

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
            navigateToCollectionReport = navigateToCollectionReport,
        )
    }
}
