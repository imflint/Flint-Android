package com.flint.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SampleResponseDto(
    @SerialName("data")
    val data: String,
)