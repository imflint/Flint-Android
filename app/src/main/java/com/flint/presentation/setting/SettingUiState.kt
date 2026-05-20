package com.flint.presentation.setting

data class SettingUiState(
    val nickname: String = "",
    val profileImageUrl: String? = null,
    val email: String = "",
    val isLogoutDialogVisible: Boolean = false,
    val isWithdrawDialogVisible: Boolean = false,
)
