package com.flint.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SampleResponseDto(
    @SerialName("data")
    val data: String
)