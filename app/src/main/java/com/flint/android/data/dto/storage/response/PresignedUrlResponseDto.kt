package com.flint.android.data.dto.storage.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresignedUrlResponseDto(
    @SerialName("uploadUrl")
    val uploadUrl: String,
    @SerialName("key")
    val key: String,
)
