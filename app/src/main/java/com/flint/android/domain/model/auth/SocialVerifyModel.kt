package com.flint.android.domain.model.auth

import com.flint.android.domain.type.ProviderType

data class SocialVerifyRequestModel(
    val provider: ProviderType,
    val accessToken: String,
)

data class SocialVerifyResponseModel(
    val isRegistered: Boolean,
    val accessToken: String?,
    val refreshToken: String?,
    val userId: String?,
    val nickName: String?,
    val tempToken: String?,
)
