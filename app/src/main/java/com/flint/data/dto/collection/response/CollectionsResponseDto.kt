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
        @SerialName("imageUrl") val imageUrl: String,
        @SerialName("contentTitle") val contentTitle: String,
        @SerialName("contentDescription") val contentDescription: String,
    )

    @Serializable
    data class Meta(
        @SerialName("type") val type: String,
        @SerialName("returned") val returned: Int,
        @SerialName("nextCursor") val nextCursor: String?,
    )
}
