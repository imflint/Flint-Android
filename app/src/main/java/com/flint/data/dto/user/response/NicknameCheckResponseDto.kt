package com.flint.data.dto.user.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NicknameCheckResponseDto(
    @SerialName("available")
    val available: Boolean
)