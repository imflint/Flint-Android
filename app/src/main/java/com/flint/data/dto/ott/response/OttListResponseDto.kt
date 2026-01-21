package com.flint.data.dto.ott.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OttListResponseDto(
    @SerialName("otts")
    val otts: List<OttItemResponseDto>,
)

@Serializable
data class OttItemResponseDto(
    @SerialName("ottId")
    val ottId: String,
    @SerialName("name")
    val name: String,
    @SerialName("logoUrl")
    val logoUrl: String,
    @SerialName("contentUrl")
    val contentUrl: String
)