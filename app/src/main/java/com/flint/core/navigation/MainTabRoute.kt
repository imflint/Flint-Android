package com.flint.core.navigation

import kotlinx.serialization.Serializable

interface Route

interface MainTabRoute: Route {
    @Serializable
    data object Home : MainTabRoute

    @Serializable
    data object Explore : MainTabRoute

    @Serializable
    data object Profile : MainTabRoute
}