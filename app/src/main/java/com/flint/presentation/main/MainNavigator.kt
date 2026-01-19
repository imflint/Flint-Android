package com.flint.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.flint.core.navigation.MainTabRoute
import com.flint.core.navigation.Route
import com.flint.presentation.collectioncreate.navigation.navigateToCollectionCreate
import com.flint.presentation.collectiondetail.navigation.navigateToCollectionDetail
import com.flint.presentation.collectionlist.navigation.navigateToCollectionList
import com.flint.presentation.explore.navigation.navigateToExplore
import com.flint.presentation.home.navigation.navigateToHome
import com.flint.presentation.login.navigation.navigateToLogin
import com.flint.presentation.onboarding.navigation.navigateToOnboarding
import com.flint.presentation.profile.navigation.navigateToProfile
import com.flint.presentation.savedcontent.navigation.navigateToSavedContentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Stable
class MainNavigator(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
) {
    val startDestination = MainTabRoute.Home

    // NavController의 Flow를 관찰하여 현재 Destination을 StateFlow로 변환
    private val currentDestination =
        navController.currentBackStackEntryFlow
            .map { it.destination }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null,
            )

    // derived state를 Composable 종속성 없이 StateFlow로 생성
    val currentTab: StateFlow<MainTab?> =
        currentDestination
            .map { destination ->
                MainTab.find { tab ->
                    destination?.hasRoute(tab::class) == true
                }
            }.stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null,
            )

    // MainTab 소속인지를 확인하며 바텀바 노출 여부 체크
    val isBottomBarVisible: StateFlow<Boolean> =
        currentDestination
            .map { destination ->
                MainTab.contains { tab ->
                    destination?.hasRoute(tab::class) == true
                }
            }.stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false,
            )

    fun navigate(tab: MainTab) {
        val navOptions =
            navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

        when (tab) {
            MainTab.HOME -> navController.navigateToHome(navOptions)
            MainTab.EXPLORE -> navController.navigateToExplore(navOptions)
            MainTab.PROFILE -> navController.navigateToProfile(navOptions)
        }
    }

    fun navigateToLogin() {
        navController.navigateToLogin(
            navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            },
        )
    }

    fun navigateToOnBoarding(tempToken: String) {
        navController.navigateToOnboarding(tempToken)
    }

    fun navigateToHome(navOptions: NavOptions? = null) {
        navController.navigateToHome(
            navOptions ?: navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            },
        )
    }

    fun navigateToCollectionList() {
        navController.navigateToCollectionList()
    }

    fun navigateToCollectionDetail(collectionId: String) {
        navController.navigateToCollectionDetail(collectionId = collectionId)
    }

    fun navigateToCollectionCreate() {
        navController.navigateToCollectionCreate()
    }

    fun navigateToSavedContent() {
        navController.navigateToSavedContentList()
    }

    fun navigateUp() {
        navController.navigateUp()
    }
}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): MainNavigator =
    remember(navController) {
        MainNavigator(navController, coroutineScope)
    }
