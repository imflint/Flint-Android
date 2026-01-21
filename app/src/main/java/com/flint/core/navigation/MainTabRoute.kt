package com.flint.core.navigation

import kotlinx.serialization.Serializable

interface MainTabRoute : Route {
    @Serializable
    data object Home : MainTabRoute

    @Serializable
    data object Explore : MainTabRoute

    @Serializable
    data class Profile(
        val userId: String? = null,
    ) : MainTabRoute
}
