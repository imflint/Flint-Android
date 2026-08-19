package com.flint.android.presentation.setting

data class SettingUiState(
    val nickname: String = "",
    val profileImageUrl: String? = null,
    val email: String? = null,
    val isLogoutDialogVisible: Boolean = false,
)
