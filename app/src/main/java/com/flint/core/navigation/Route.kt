package com.flint.core.navigation

import kotlinx.serialization.Serializable

interface Route {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data class OnboardingProfile(
        val tempToken: String
    ) : Route

    @Serializable
    data object OnboardingFilm : Route

    @Serializable
    data object OnboardingOtt : Route

    @Serializable
    data object OnboardingDone : Route

    @Serializable
    data object CollectionList : Route

    @Serializable
    data class CollectionDetail(
        val collectionId: String,
    ) : Route

    @Serializable
    data object CollectionCreate : Route

    @Serializable
    data object SavedFilmList : Route

    @Serializable
    data object AddFilm : Route
}
