package com.flint.android.presentation.onboarding

import android.net.Uri
import com.flint.android.core.common.util.UiState
import com.flint.android.domain.model.search.SearchContentItemModel
import com.flint.android.domain.model.terms.TermModel
import com.flint.android.domain.type.OttType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class OnboardingTermsUiState(
    val termsState: UiState<List<TermModel>> = UiState.Empty,
    val agreedTermsIds: List<String> = emptyList(),
    // 체크/펼침 상태는 화면이 아니라 여기에 둔다. 화면 로컬 remember에 두면
    // '자세히 보기'나 다음 온보딩 단계로 이동할 때 컴포지션이 해제되며 초기화된다.
    val checkedTermIds: Set<String> = emptySet(),
    val expandedTermIds: Set<String> = emptySet(),
) {
    val terms: List<TermModel>
        get() = (termsState as? UiState.Success)?.data ?: emptyList()

    fun isChecked(termId: String): Boolean = termId in checkedTermIds

    fun isExpanded(termId: String): Boolean = termId in expandedTermIds

    val isAllChecked: Boolean
        get() = terms.isNotEmpty() && terms.all { it.id in checkedTermIds }

    // 필수 약관이 모두 체크되어야 동의하기 버튼이 활성화된다
    val canProceed: Boolean
        get() = terms.isNotEmpty() && terms.filter { it.required }.all { it.id in checkedTermIds }

    // 약관 목록 순서를 따라 체크된 것만 수집한다
    val agreedIds: List<String>
        get() = terms.filter { it.id in checkedTermIds }.map { it.id }

    fun toggleChecked(termId: String): OnboardingTermsUiState = copy(
        checkedTermIds = if (termId in checkedTermIds) {
            checkedTermIds - termId
        } else {
            checkedTermIds + termId
        },
    )

    fun toggleAllChecked(): OnboardingTermsUiState = copy(
        checkedTermIds = if (isAllChecked) emptySet() else terms.map { it.id }.toSet(),
    )

    fun toggleExpanded(termId: String): OnboardingTermsUiState = copy(
        expandedTermIds = if (termId in expandedTermIds) {
            expandedTermIds - termId
        } else {
            expandedTermIds + termId
        },
    )
}

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