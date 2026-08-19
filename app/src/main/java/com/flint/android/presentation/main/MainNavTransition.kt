package com.flint.android.presentation.main

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import com.flint.android.core.navigation.Route

private const val SLIDE_DURATION = 300
private const val FADE_DURATION = 220

/**
 * 뒤로 밀려나는 화면은 새 화면의 1/3 만 움직인다.
 * 두 화면이 같은 속도로 지나가면 겹쳐 보여서, 깊이감을 주기 위해 차이를 둔다.
 */
private const val PARALLAX_DIVIDER = 3

private val slideSpec = tween<IntOffset>(durationMillis = SLIDE_DURATION, easing = FastOutSlowInEasing)

private val fadeSpec = tween<Float>(durationMillis = FADE_DURATION, easing = FastOutSlowInEasing)

/**
 * 탭 간 이동과 스플래시 전후에는 방향성이 없으므로 슬라이드 대신 페이드를 쓴다.
 * (탭 A → 탭 B 를 오른쪽에서 밀어 넣으면 계층이 깊어진 것처럼 보인다)
 */
private fun AnimatedContentTransitionScope<NavBackStackEntry>.isLateralMove(): Boolean {
    val bothTabs = initialState.isMainTab() && targetState.isMainTab()
    val involvesSplash = initialState.isSplash() || targetState.isSplash()
    return bothTabs || involvesSplash
}

private fun NavBackStackEntry.isMainTab(): Boolean =
    MainTab.contains { tabRoute -> destination.hasRoute(tabRoute::class) }

private fun NavBackStackEntry.isSplash(): Boolean = destination.hasRoute(Route.Splash::class)

/** 새 화면이 오른쪽에서 들어온다. */
internal fun AnimatedContentTransitionScope<NavBackStackEntry>.flintEnterTransition(): EnterTransition =
    if (isLateralMove()) {
        fadeIn(fadeSpec)
    } else {
        slideInHorizontally(slideSpec) { width -> width } + fadeIn(fadeSpec)
    }

/** 기존 화면이 왼쪽으로 조금 밀려난다. */
internal fun AnimatedContentTransitionScope<NavBackStackEntry>.flintExitTransition(): ExitTransition =
    if (isLateralMove()) {
        fadeOut(fadeSpec)
    } else {
        slideOutHorizontally(slideSpec) { width -> -width / PARALLAX_DIVIDER } + fadeOut(fadeSpec)
    }

/** 뒤로가기 — 아래 화면이 왼쪽에서 제자리로 돌아온다. */
internal fun AnimatedContentTransitionScope<NavBackStackEntry>.flintPopEnterTransition(): EnterTransition =
    if (isLateralMove()) {
        fadeIn(fadeSpec)
    } else {
        slideInHorizontally(slideSpec) { width -> -width / PARALLAX_DIVIDER } + fadeIn(fadeSpec)
    }

/** 뒤로가기 — 현재 화면이 오른쪽으로 완전히 빠진다. */
internal fun AnimatedContentTransitionScope<NavBackStackEntry>.flintPopExitTransition(): ExitTransition =
    if (isLateralMove()) {
        fadeOut(fadeSpec)
    } else {
        slideOutHorizontally(slideSpec) { width -> width } + fadeOut(fadeSpec)
    }
