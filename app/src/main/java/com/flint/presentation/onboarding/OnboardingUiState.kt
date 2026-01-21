package com.flint.presentation.onboarding

import com.flint.domain.model.search.SearchContentItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class OnboardingProfileUiState(
    val nickname: String = "",
    val isValid: Boolean = false,
    val isNicknameAvailable: Boolean? = null,
) {
    companion object {
        const val MAX_LENGTH = 8
        const val MIN_LENGTH = 2
    }

    //다믐단게 활성화
    val canProceed: Boolean
        get() = isValid && isNicknameAvailable == true
}

data class OnboardingContentUiState(
    val searchKeyword: String = "",
    val searchResults: ImmutableList<SearchContentItemModel> = persistentListOf(),
    val selectedContents: ImmutableList<SearchContentItemModel> = persistentListOf(),
    val isSearching: Boolean = false,
) {
    companion object {
        const val REQUIRED_SELECTION_COUNT = 7
    }

    val canProceed: Boolean
        get() = selectedContents.size == REQUIRED_SELECTION_COUNT

    val isContentSelected: (String) -> Boolean = { contentId ->
        selectedContents.any { it.id == contentId }
    }
}