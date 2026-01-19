package com.flint.domain.model.auth

import com.flint.domain.type.ProviderType

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
