package com.flint.presentation.onboarding

import com.flint.core.common.util.UiState
import com.flint.domain.model.search.SearchContentItemModel
import com.flint.domain.type.OttType
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

    //다음단계 활성화
    val canProceed: Boolean
        get() = isValid && isNicknameAvailable == true
}

data class OnboardingContentUiState(
    val searchKeyword: String = "",
    val searchResults: UiState<ImmutableList<SearchContentItemModel>> = UiState.Empty,
    val selectedContents: ImmutableList<SearchContentItemModel> = persistentListOf(),
    val isSearching: Boolean = false,
) {
    companion object {
        const val REQUIRED_SELECTION_COUNT = 7

        private val STEP_QUESTIONS = listOf(
            "이번 달 가장 재미있었던 작품은?",
            "여러번 정주행 했던 작품은 무엇인가요?",
            "좋아하는 인물이 등장하는 작품은 무엇인가요?",
            "요즘 밥 먹으면서 자주 보는 작품은 무엇인가요?",
            "\"이건 꼭 봐\"라고 말했던 작품은 무엇인가요?",
            "계절마다 생각나는 작품은 무엇인가요?",
            "어렸을 적 즐겨봤던 추억의 작품은 무엇인가요?"
        )
    }

    val currentStepQuestion: String
        get() = STEP_QUESTIONS.getOrElse(selectedContents.size) { STEP_QUESTIONS.first() }

    val canProceed: Boolean
        get() = selectedContents.size == REQUIRED_SELECTION_COUNT

    val isContentSelected: (String) -> Boolean = { contentId ->
        selectedContents.any { it.id == contentId }
    }
}

data class OnboardingOttUiState(
    val selectedOtts: ImmutableList<OttType> = persistentListOf(),
) {
    companion object {
        const val MIN_SELECTION_COUNT = 1
    }

    val canProceed: Boolean
        get() = selectedOtts.size >= MIN_SELECTION_COUNT

    val isOttSelected: (OttType) -> Boolean = { ottType ->
        selectedOtts.contains(ottType)
    }
}

data class OnboardingSignupUiState(
    val signupState: UiState<Unit> = UiState.Empty,
) {
    val isLoading: Boolean
        get() = signupState is UiState.Loading

    val isSuccess: Boolean
        get() = signupState is UiState.Success
}