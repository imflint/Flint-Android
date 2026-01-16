package com.flint.data.dto.request.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialVerifyRequestDto (
    @SerialName("provider")
    val provider : String,
    @SerialName("code")
    val code : String,
)