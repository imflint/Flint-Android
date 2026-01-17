package com.flint.data.model.auth

import com.flint.core.common.type.ProviderType

data class SocialVerifyRequestModel(
    val provider: ProviderType,
    val accessToken: String,
)

data class SocialVerifyResponseModel(
    val isRegistered: Boolean,
    val accessToken: String?,
    val refreshToken: String?,
    val userId: Long?,
    val tempToken: String?,
)
