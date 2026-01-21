package com.flint.data.dto.collection.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionsResponseDto(
    @SerialName("data") val data: List<Collection>,
    @SerialName("meta") val meta: Meta,
) {
    @Serializable
    data class Collection(
        @SerialName("collectionId") val collectionId: String,
        @SerialName("createdAt") val createdAt: String,
        @SerialName("description") val description: String,
        @SerialName("imageUrl") val imageUrl: String,
        @SerialName("title") val title: String,
    )

    @Serializable
    data class Meta(
        @SerialName("currentPage") val currentPage: Int,
        @SerialName("nextCursor") val nextCursor: String,
        @SerialName("returned") val returned: Int,
        @SerialName("totalElements") val totalElements: Int,
        @SerialName("totalPages") val totalPages: Int,
        @SerialName("type") val type: String,
    )
}
