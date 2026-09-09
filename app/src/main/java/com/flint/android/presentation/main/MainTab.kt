package com.flint.android.presentation.main

import androidx.annotation.DrawableRes
import com.flint.android.R
import com.flint.android.core.analytics.BottomNavigationTab
import com.flint.android.core.navigation.MainTabRoute
import com.flint.android.core.navigation.Route

enum class MainTab(
    @DrawableRes val iconResId: Int,
    val route: MainTabRoute,
    val label: String,
) {
    HOME(
        iconResId = R.drawable.ic_home_empty,
        route = MainTabRoute.Home,
        label = "홈",
    ),
    EXPLORE(
        iconResId = R.drawable.ic_explore_empty,
        route = MainTabRoute.Explore,
        label = "탐색",
    ),
    PROFILE(
        iconResId = R.drawable.ic_my_empty,
        route = MainTabRoute.Profile,
        label = "MY",
    ),
    ;

    companion object {
        fun find(predicate: (MainTabRoute) -> Boolean): MainTab? =
            MainTab.entries.find { predicate(it.route) }

        fun contains(predicate: (Route) -> Boolean): Boolean =
            MainTab.entries.map { it.route }.any { predicate(it) }
    }
}

/** 정의서의 tab_name 값으로 변환한다. 화면 enum 과 분석 값이 1:1 이 아닐 수 있어 매핑을 분리해 둔다. */
fun MainTab.toAnalyticsTab(): BottomNavigationTab =
    when (this) {
        MainTab.HOME -> BottomNavigationTab.HOME
        MainTab.EXPLORE -> BottomNavigationTab.EXPLORE
        MainTab.PROFILE -> BottomNavigationTab.MY
    }
