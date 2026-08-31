package com.flint.android.presentation.main

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.android.core.analytics.FlintEvent
import com.flint.android.core.analytics.LocalAnalyticsTracker
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.presentation.main.component.MainBottomBar
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen(navigator: MainNavigator) {
    val isBottomBarVisible by navigator.isBottomBarVisible.collectAsStateWithLifecycle()
    val currentTab by navigator.currentTab.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val analyticsTracker = LocalAnalyticsTracker.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Scaffold(
            containerColor = FlintTheme.colors.background,
            bottomBar = {
                MainBottomBar(
                    visible = isBottomBarVisible,
                    tabs = MainTab.entries.toImmutableList(),
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        analyticsTracker.track(FlintEvent.ClickBottomNavigation(tab.toAnalyticsTab()))
                        navigator.navigate(tab)
                    },
                )
            },
        ) { paddingValues ->
            MainNavHost(
                navigator = navigator,
                paddingValues = paddingValues,
            )
        }
    }
}
