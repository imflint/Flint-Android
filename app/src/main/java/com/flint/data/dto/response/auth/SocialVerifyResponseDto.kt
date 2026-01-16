package com.flint.data.dto.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialVerifyResponseDto(
    @SerialName("isRegistered")
    val isRegistered : Boolean,
    @SerialName("accessToken")
    val accessToken : String,
    @SerialName("refreshToken")
    val refreshToken : String,
    @SerialName("userId")
    val userId : Long,
    @SerialName("tempToken")
    val tempToken : String,
)