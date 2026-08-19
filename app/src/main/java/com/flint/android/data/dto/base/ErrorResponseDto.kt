package com.flint.android.data.dto.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDto(
    @SerialName("errorCode")
    val errorCode: String? = null,
    @SerialName("message")
    val message: String? = null,
)
