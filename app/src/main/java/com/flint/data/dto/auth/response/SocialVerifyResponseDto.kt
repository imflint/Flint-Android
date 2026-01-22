package com.flint.data.dto.auth.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialVerifyResponseDto(
    @SerialName("isRegistered")
    val isRegistered: Boolean,
    @SerialName("accessToken")
    val accessToken: String? = null,
    @SerialName("refreshToken")
    val refreshToken: String? = null,
    @SerialName("userId")
    val userId: String? = null,
    @SerialName("nickname")
    val nickname: String? = null,
    @SerialName("tempToken")
    val tempToken: String? = null,
)
