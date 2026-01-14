package com.flint.core.navigation

import kotlinx.serialization.Serializable

interface Route {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object OnboardingProfile : Route

    @Serializable
    data object OnboardingFilm : Route

    @Serializable
    data object OnboardingOtt : Route

    @Serializable
    data object CollectionList : Route

    @Serializable
    data object CollectionDetail : Route

    @Serializable
    data object CollectionCreate : Route

    @Serializable
    data object SavedFilmList : Route

    @Serializable
    data object AddFilm : Route
}
