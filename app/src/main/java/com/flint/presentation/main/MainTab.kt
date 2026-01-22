package com.flint.presentation.main

import androidx.annotation.DrawableRes
import com.flint.R
import com.flint.core.navigation.MainTabRoute
import com.flint.core.navigation.Route

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
