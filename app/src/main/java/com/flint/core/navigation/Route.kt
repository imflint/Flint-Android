package com.flint.core.navigation

import com.flint.core.navigation.model.CollectionListRouteType
import kotlinx.serialization.Serializable

interface Route {
    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object OnboardingProfile : Route

    @Serializable
    data class OnboardingGraph(
        val tempToken: String
    ) : Route

    @Serializable
    data object OnboardingTerms : Route

    @Serializable
    data object OnboardingContent : Route

    @Serializable
    data object OnboardingOtt : Route

    @Serializable
    data object OnboardingDone : Route

    @Serializable
    data class CollectionList(
        val routeType: CollectionListRouteType,
        val userId: String? = null
    ) : Route

    @Serializable
    data class CollectionDetail(
        val collectionId: String,
        val targetImageUrl: String? = null,
        val showEditSuccessToast: Boolean = false,
    ) : Route

    @Serializable
    data class CollectionReport(
        val collectionId: String,
    ) : Route

    @Serializable
    data object CollectionCreate : Route

    @Serializable
    data class CollectionCreateGraph(
        val collectionId: String? = null,
    ) : Route

    @Serializable
    data object SavedContentList : Route

    @Serializable
    data object AddContent : Route

    @Serializable
    data class Profile(
        val userId: String? = null,
    ) : Route

    @Serializable
    data object Setting : Route

    @Serializable
    data object EditProfile : Route

    @Serializable
    data object Withdraw : Route

    @Serializable
    data object WithdrawComplete : Route
}
