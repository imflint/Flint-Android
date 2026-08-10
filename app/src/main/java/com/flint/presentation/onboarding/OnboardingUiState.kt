package com.flint.presentation.onboarding

import android.net.Uri
import com.flint.core.common.util.UiState
import com.flint.domain.model.search.SearchContentItemModel
import com.flint.domain.model.terms.TermModel
import com.flint.domain.type.OttType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class OnboardingTermsUiState(
    val termsState: UiState<List<TermModel>> = UiState.Empty,
    val agreedTermsIds: List<String> = emptyList(),
)

enum class NicknameErrorType {
    DUPLICATE,      // 이미 사용 중인 닉네임
    INVALID_FORMAT  // 한글, 영문, 숫자 외 문자 포함
}

data class OnboardingProfileUiState(
    val nickname: String = "",
    val isValid: Boolean = false,
    val isFormatValid: Boolean = true,
    val isNicknameAvailable: Boolean? = null,
    val nicknameErrorType: NicknameErrorType? = null,
    val profileImageUri: Uri? = null,
) {
    companion object {
        const val MAX_LENGTH = 8
        const val MIN_LENGTH = 2
      
        // 완성된 한글 음절(가-힣), 영문, 숫자만 허용. 자음/모음만 단독으로 입력된 경우 "ㅇㄴㄹ", "ㅏㅏ" 같은 입력도 형식 오류로 처리된다.
        private val NICKNAME_REGEX = Regex("^[가-힣a-zA-Z0-9]+$")

        fun isValidFormat(nickname: String): Boolean {
            return nickname.isEmpty() || NICKNAME_REGEX.matches(nickname)
        }
    }

    val hasError: Boolean
        get() = nicknameErrorType != null

    val errorMessage: String?
        get() = when (nicknameErrorType) {
            NicknameErrorType.DUPLICATE -> "이미 사용 중인 닉네임입니다"
            NicknameErrorType.INVALID_FORMAT -> "사용할 수 없는 닉네임입니다"
            null -> null
        }

    // 형식 오류(자모 단독 입력 포함) 여부와 무관하게 2자 이상이면 활성화 — 형식 검증은 버튼 클릭 시 수행
    val canCheckNickname: Boolean
        get() = isValid

    //다음단계 활성화
    val canProceed: Boolean
        get() = isValid && isFormatValid && isNicknameAvailable == true
}

data class OnboardingContentUiState(
    val searchKeyword: String = "",
    val searchResults: UiState<ImmutableList<SearchContentItemModel>> = UiState.Empty,
    val selectedContents: ImmutableList<SearchContentItemModel> = persistentListOf(),
    val isSearching: Boolean = false,
    val selectedGenres: Set<String> = emptySet(),
    val nextCursor: String? = null,
    val isLoadingMore: Boolean = false,
) {
    companion object {
        const val REQUIRED_SELECTION_COUNT = 7

        // 표시용(Korean) → API enum 매핑
        val GENRES: Map<String, String> = linkedMapOf(
            "액션" to "ACTION",
            "로맨스" to "ROMANCE",
            "SF" to "SCIENCE_FICTION",
            "드라마" to "DRAMA",
            "코미디" to "COMEDY",
            "호러" to "HORROR",
        )
    }

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