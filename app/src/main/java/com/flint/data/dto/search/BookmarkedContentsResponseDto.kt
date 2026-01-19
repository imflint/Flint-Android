package com.flint.data.dto.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookmarkedContentsResponseDto(
    @SerialName("data")
    val data: List<BookmarkedContentsItemResponseDto>,
    @SerialName("meta")
    val meta: List<BookmarkedContentsMetaItemResponseDto>
)

@Serializable
data class BookmarkedContentsItemResponseDto(
    @SerialName("bookmarkId")
    val bookmarkId: Long,
    @SerialName("contentId")
    val contentId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("author")
    val author: String,
    @SerialName("posterUrl")
    val posterUrl: String,
    @SerialName("year")
    val year: Int
)

@Serializable
data class BookmarkedContentsMetaItemResponseDto(
    @SerialName("type")
    val type: String,
    @SerialName("returned")
    val returned: Int,
    @SerialName("currentPage")
    val currentPage: Int,
    @SerialName("totalPages")
    val totalPages: Int,
    @SerialName("totalElements")
    val totalElements: Int,
    @SerialName("nextCursor")
    val nextCursor: String,
)