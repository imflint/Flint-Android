package com.flint.data.dto.exploration.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExplorationResponseDto(
    @SerialName("items") val items: List<Item>,
    // IN_PROGRESS / END / EMPTY
    @SerialName("state") val state: String,
    @SerialName("hasNext") val hasNext: Boolean,
) {
    @Serializable
    data class Item(
        @SerialName("contentId") val contentId: String,
        @SerialName("title") val title: String,
        @SerialName("description") val description: String,
        @SerialName("imageUrl") val imageUrl: String,
        @SerialName("year") val year: Int,
        @SerialName("collectionId") val collectionId: String,
    )
}
