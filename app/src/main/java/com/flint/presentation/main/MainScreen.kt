package com.flint.presentation.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import com.flint.presentation.explore.navigation.exploreNavGraph
import com.flint.presentation.home.navigation.homeNavGraph
import com.flint.presentation.main.component.MainBottomBar
import com.flint.presentation.profile.navigation.profileNavGraph
import kotlinx.collections.immutable.toImmutableList

import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun MainScreen(navigator: MainNavigator) {
    val isBottomBarVisible by navigator.isBottomBarVisible.collectAsStateWithLifecycle()
    val currentTab by navigator.currentTab.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = FlintTheme.colors.background,
        bottomBar = {
            MainBottomBar(
                visible = isBottomBarVisible,
                tabs = MainTab.entries.toImmutableList(),
                currentTab = currentTab,
                onTabSelected = navigator::navigate
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination
        ) {
            homeNavGraph(
                paddingValues = paddingValues
            )
            exploreNavGraph(
                paddingValues = paddingValues
            )
            profileNavGraph(
                paddingValues = paddingValues
            )
        }
    }
}
