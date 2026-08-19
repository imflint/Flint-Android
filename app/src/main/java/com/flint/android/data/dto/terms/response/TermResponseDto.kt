package com.flint.android.data.dto.terms.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TermResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("type")
    val type: String,
    @SerialName("version")
    val version: Int,
    @SerialName("title")
    val title: String,
    @SerialName("content")
    val content: String,
    @SerialName("required")
    val required: Boolean,
    @SerialName("activeAt")
    val activeAt: String,
)
