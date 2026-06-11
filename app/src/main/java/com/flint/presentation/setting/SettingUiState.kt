package com.flint.presentation.setting

data class SettingUiState(
    val nickname: String = "",
    val profileImageUrl: String? = null,
    val isLogoutDialogVisible: Boolean = false,
)
