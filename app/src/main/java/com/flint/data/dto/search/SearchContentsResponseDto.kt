package com.flint.data.dto.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchContentsResponseDto(
    @SerialName("contents")
    val contents: List<Content>
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
        val year: Int
    )
}