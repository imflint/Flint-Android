package com.flint.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContentSearchResponseDto(
    @SerialName("data") val contents: List<ContentItemDto>,
    @SerialName("meta") val meta: PageMetaDto,
)

// TODO: null 처리를 어떻게?  어떤거가 null가능인지
@Serializable
data class ContentItemDto(
    @SerialName("contentId") val contentId: Long?,
    @SerialName("title") val title: String?,
    @SerialName("author") val author: String?,
    @SerialName("posterUrl") val posterUrl: String?,
    @SerialName("year") val year: Int?,
)

@Serializable
data class PageMetaDto(
    @SerialName("type") val type: String,
    @SerialName("returned") val returned: Int,
    @SerialName("currentPage") val currentPage: Int,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("totalElements") val totalElements: Int,
    @SerialName("nextCursor") val nextCursor: String?,
)
