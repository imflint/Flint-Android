package com.flint.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.flint.core.navigation.MainTabRoute
import com.flint.presentation.explore.navigation.navigateToExplore
import com.flint.presentation.home.navigation.navigateToHome
import com.flint.presentation.main.component.MainTab
import com.flint.presentation.profile.navigation.navigateToProfile

class MainNavigator(
    val navController: NavHostController
){
    val startDestination = MainTabRoute.Home

    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState()
            .value?.destination


    val currentTab: MainTab?
        @Composable get() {
            val destination = currentDestination

            return MainTab.find { tabRoute ->
                destination?.hasRoute(tabRoute::class) == true
            }
        }

    fun navigate(tabRoute: MainTab) {
        val navOptions = navOptions {
            launchSingleTop = true
            restoreState = true
        }

        when (tabRoute){
            MainTab.HOME -> navController.navigateToHome(navOptions)
            MainTab.EXPLORE -> navController.navigateToExplore(navOptions)
            MainTab.PROFILE -> navController.navigateToProfile(navOptions)
        }
    }


}

@Composable
fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}