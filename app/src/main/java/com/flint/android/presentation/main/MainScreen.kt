package com.flint.android.presentation.main

import android.os.SystemClock
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.android.core.analytics.FlintEvent
import com.flint.android.core.analytics.LocalAnalyticsTracker
import com.flint.android.core.common.extension.findActivity
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.presentation.main.component.MainBottomBar
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainScreen(navigator: MainNavigator) {
    val isBottomBarVisible by navigator.isBottomBarVisible.collectAsStateWithLifecycle()
    val currentTab by navigator.currentTab.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val analyticsTracker = LocalAnalyticsTracker.current

    // NavHost보다 먼저 등록해서, 각 화면 안의 BackHandler가 있으면 그쪽이 먼저 처리하게 둔다.
    MainTabBackHandler(
        currentTab = currentTab,
        navigateToHome = { navigator.navigate(MainTab.HOME) },
    )

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

private const val EXIT_BACK_PRESS_INTERVAL_MS = 2_000L

// 탭 루트에서의 뒤로가기. 탐색·MY는 홈으로 보내고, 홈은 두 번 연속 눌러야 앱을 종료한다.
// 탭 위에 다른 화면이 쌓여 있으면 currentTab이 null이라 NavHost의 기본 뒤로가기가 처리한다.
@Composable
private fun MainTabBackHandler(
    currentTab: MainTab?,
    navigateToHome: () -> Unit,
) {
    val context = LocalContext.current
    var lastBackPressedAt by remember { mutableLongStateOf(0L) }

    BackHandler(enabled = currentTab != null) {
        if (currentTab != MainTab.HOME) {
            navigateToHome()
            return@BackHandler
        }

        val now = SystemClock.elapsedRealtime()
        if (now - lastBackPressedAt <= EXIT_BACK_PRESS_INTERVAL_MS) {
            context.findActivity().finish()
        } else {
            lastBackPressedAt = now
            Toast.makeText(context, "한 번 더 누르면 앱이 종료돼요", Toast.LENGTH_SHORT).show()
        }
    }
}
