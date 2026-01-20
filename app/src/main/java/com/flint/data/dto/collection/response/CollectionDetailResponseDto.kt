package com.flint.data.dto.collection.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDetailResponseDto(
    @SerialName("author") val author: Author,
    @SerialName("contents") val contents: List<Content>,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("description") val description: String,
    @SerialName("id") val id: String,
    @SerialName("thumbnailUrl") val thumbnailUrl: String,
    @SerialName("isBookmarked") val isBookmarked: Boolean,
    @SerialName("title") val title: String,
) {
    @Serializable
    data class Author(
        @SerialName("id") val id: String,
        @SerialName("nickname") val nickname: String,
        @SerialName("profileUrl") val profileUrl: String?,
        @SerialName("userRole") val userRole: String,
    )

    @Serializable
    data class Content(
        @SerialName("director") val director: String,
        @SerialName("bookmarkCount") val bookmarkCount: Int,
        @SerialName("id") val id: String,
        @SerialName("isBookmarked") val isBookmarked: Boolean,
        @SerialName("isSpoiler") val isSpoiler: Boolean,
        @SerialName("reason") val reason: String,
        @SerialName("imageUrl") val imageUrl: String,
        @SerialName("title") val title: String,
        @SerialName("year") val year: Int,
    )
}