package com.flint.android.data.dto.search

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
        val id: String? = null,
        @SerialName("title")
        val title: String? = null,
        @SerialName("author")
        val author: String? = null,
        @SerialName("posterUrl")
        val posterUrl: String? = null,
        @SerialName("year")
        val year: Int? = null,
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
