package com.flint.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import com.flint.presentation.collectioncreate.navigation.collectionCreateNavGraph
import com.flint.presentation.collectiondetail.navigation.collectionDetailNavGraph
import com.flint.presentation.collectionlist.navigation.collectionListNavGraph
import com.flint.presentation.explore.navigation.exploreNavGraph
import com.flint.presentation.home.navigation.homeNavGraph
import com.flint.presentation.login.navigation.loginNavGraph
import com.flint.presentation.onboarding.navigation.onBoardingNavGraph
import com.flint.presentation.profile.navigation.profileNavGraph
import com.flint.presentation.savedfilm.navigation.savedFilmListNavGraph
import com.flint.presentation.splash.navigation.splashNavGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = Color.White),
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
        ) {
            splashNavGraph(
                paddingValues = paddingValues,
                onNavigateToLogin = navigator::navigateToLogin,
                onNavigateToHome = navigator::navigateToHome,
            )

            loginNavGraph(
                paddingValues = paddingValues,
                onNavigateToOnBoarding = navigator::navigateToOnBoarding,
                onNavigateToHome = navigator::navigateToHome,
            )

            onBoardingNavGraph(
                paddingValues = paddingValues,
                onNavigateToHome = navigator::navigateToHome,
                navController = navigator.navController,
            )

            homeNavGraph(
                paddingValues = paddingValues,
                navigateToCollectionList = navigator::navigateToCollectionList,
                navigateToCollectionDetail = navigator::navigateToCollectionDetail,
                navigateToCollectionCreate = navigator::navigateToCollectionCreate,
            )

            collectionListNavGraph(
                paddingValues = paddingValues,
                navigateToCollectionDetail = navigator::navigateToCollectionDetail,
            )

            collectionDetailNavGraph(
                paddingValues = paddingValues,
                navigateToCollectionList = navigator::navigateToCollectionList,
            )

            collectionCreateNavGraph(
                paddingValues = paddingValues,
                navController = navigator.navController,
            )

            savedFilmListNavGraph(
                paddingValues = paddingValues,
            )

            exploreNavGraph(
                paddingValues = paddingValues,
                navigateToCollectionDetail = navigator::navigateToCollectionDetail,
                navigateToCollectionCreate = navigator::navigateToCollectionCreate,
            )

            profileNavGraph(
                paddingValues = paddingValues,
                navigateToCollectionList = navigator::navigateToCollectionList,
                navigateToSavedFilmList = navigator::navigateToSavedFilm,
                navigateToCollectionDetail = navigator::navigateToCollectionDetail,
            )
        }
    }
}
