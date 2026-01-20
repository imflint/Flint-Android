package com.flint.data.dto.collection.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionCreateRequestDto(
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("isPublic")
    val isPublic: Boolean,
    @SerialName("contentList")
    val contentList: List<Content>,
) {
    @Serializable
    data class Content(
        @SerialName("contentId")
        val contentId: Int,
        @SerialName("isSpoiler")
        val isSpoiler: Boolean,
        @SerialName("reason")
        val reason: String,
    )
}
