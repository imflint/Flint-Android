package com.flint.presentation.setting.editprofile

data class EditProfileUiState(
    val initialNickname: String = "",
    val nickname: String = "",
    val profileImageUrl: String? = null,
    val isFormatValid: Boolean = true,
    val isNicknameAvailable: Boolean? = null,
    val nicknameErrorType: EditProfileNicknameErrorType? = null,
) {
    companion object {
        const val MAX_LENGTH = 8
        const val MIN_LENGTH = 2
        private val NICKNAME_REGEX = Regex("^[가-힣a-zA-Z]+$")

        fun isValidFormat(nickname: String): Boolean =
            nickname.isEmpty() || NICKNAME_REGEX.matches(nickname)
    }

    val isNicknameChanged: Boolean get() = nickname != initialNickname
    val isLengthValid: Boolean get() = nickname.length >= MIN_LENGTH

    val hasError: Boolean get() = nicknameErrorType != null
    val errorMessage: String? get() = when (nicknameErrorType) {
        EditProfileNicknameErrorType.DUPLICATE -> "이미 사용 중인 닉네임입니다"
        EditProfileNicknameErrorType.INVALID_FORMAT -> "사용할 수 없는 닉네임입니다"
        null -> null
    }

    val canCheckNickname: Boolean get() = isLengthValid && isFormatValid && isNicknameChanged

    val canComplete: Boolean get() = when {
        !isNicknameChanged -> true
        !isLengthValid || !isFormatValid -> false
        else -> isNicknameAvailable == true
    }
}

enum class EditProfileNicknameErrorType {
    DUPLICATE,
    INVALID_FORMAT,
}
