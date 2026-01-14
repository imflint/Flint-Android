package com.flint.presentation.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.main.component.MainBottomBar
import kotlinx.collections.immutable.toImmutableList

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
                onTabSelected = navigator::navigate,
            )
        },
    ) { paddingValues ->
        MainNavHost(
            navigator = navigator,
            paddingValues = paddingValues,
        )
    }
}
