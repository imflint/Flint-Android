package com.flint.presentation.login.data

data class VerifyStatusData(
    val isRegistered: Boolean,
    val tempToken: String?
)
