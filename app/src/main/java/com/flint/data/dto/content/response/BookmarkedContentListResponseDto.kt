package com.flint.data.dto.content.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookmarkedContentListResponseDto(
    @SerialName("totalCount")
    val totalCount: Int = 0,
    @SerialName("contents")
    val contents: List<BookmarkedContentResponseDto>
)

@Serializable
data class BookmarkedContentResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("year")
    val year: Int,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("bookmarkCount")
    val bookmarkCount: Int = 0,
    @SerialName("isBookmarked")
    val isBookmarked: Boolean = false,
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
