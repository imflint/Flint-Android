package com.flint.data.dto.content.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookmarkedContentListResponseDto(
    @SerialName("contentId")
    val contentId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("year")
    val year: Int,
    @SerialName("getOttSimpleList")
    val getOttSimpleList: List<OttSimpleResponseDto>
)

@Serializable
data class OttSimpleResponseDto(
    @SerialName("ottName")
    val ottName: String,
    @SerialName("logoUrl")
    val logoUrl: String
)