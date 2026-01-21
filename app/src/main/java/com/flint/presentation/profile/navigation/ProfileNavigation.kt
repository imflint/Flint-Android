package com.flint.presentation.profile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.flint.core.navigation.MainTabRoute
import com.flint.core.navigation.model.CollectionListRouteType
import com.flint.presentation.profile.ProfileRoute

fun NavController.navigateToProfile(
    userId: String? = null,
    navOptions: NavOptions? = null,
) {
    navigate(MainTabRoute.Profile(userId = userId), navOptions)
}

fun NavGraphBuilder.profileNavGraph(
    paddingValues: PaddingValues,
    navigateToCollectionList: (routeType: CollectionListRouteType) -> Unit,
    navigateToSavedContentList: () -> Unit,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
) {
    composable<MainTabRoute.Profile> {
        ProfileRoute(
            paddingValues = paddingValues,
            navigateToCollectionList = navigateToCollectionList,
            navigateToSavedContentList = navigateToSavedContentList,
            navigateToCollectionDetail = navigateToCollectionDetail,
        )
    }
}
