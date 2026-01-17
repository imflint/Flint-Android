package com.flint.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SampleRequestDto(
    @SerialName("data")
    val data: String,
)