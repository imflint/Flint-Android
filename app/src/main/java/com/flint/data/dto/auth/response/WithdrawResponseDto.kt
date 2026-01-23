package com.flint.data.dto.auth.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WithdrawResponseDto(
    @SerialName("status")
    val status: Int,
    @SerialName("message")
    val message: String,
)
