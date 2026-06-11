package com.flint.data.dto.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseEmptyResponse(
    @SerialName("status")
    val status: Int,
    @SerialName("message")
    val message: String,
)
