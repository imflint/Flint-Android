package com.flint.presentation.main.component

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.flint.core.navigation.MainTabRoute
import com.flint.R
import com.flint.core.navigation.Route

enum class MainTab(
    @DrawableRes val icon: Int,
    val route: MainTabRoute,
    val label: String,
){
    HOME(
        icon = R.drawable.ic_home_empty,
        route = MainTabRoute.Home,
        label = "홈",
    ),

    EXPLORE(
        icon = R.drawable.ic_explore_empty,
        route = MainTabRoute.Explore,
        label = "탐색",
    ),

    PROFILE(
        icon = R.drawable.ic_my_empty,
        route = MainTabRoute.Profile,
        label = "프로필",
    );

    companion object {
        fun find(predicate: (MainTabRoute) -> Boolean): MainTab? {
            return entries.find { predicate(it.route) }
        }

        fun contains(predicate: (Route) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }
    }
}