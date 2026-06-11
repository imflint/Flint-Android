package com.flint.data.dto.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchContentsResponseDto(
    @SerialName("data")
    val data: List<Content>,
    @SerialName("meta")
    val meta: Meta? = null,
) {
    @Serializable
    data class Content(
        @SerialName("id")
        val id: String,
        @SerialName("title")
        val title: String,
        @SerialName("author")
        val author: String,
        @SerialName("posterUrl")
        val posterUrl: String,
        @SerialName("year")
        val year: Int,
    )

    @Serializable
    data class Meta(
        @SerialName("type")
        val type: String? = null,
        @SerialName("returned")
        val returned: Int? = null,
        @SerialName("nextCursor")
        val nextCursor: String? = null,
        @SerialName("page")
        val page: Int? = null,
        @SerialName("size")
        val size: Int? = null,
        @SerialName("totalElements")
        val totalElements: String? = null,
        @SerialName("totalPages")
        val totalPages: Int? = null,
    )
}
