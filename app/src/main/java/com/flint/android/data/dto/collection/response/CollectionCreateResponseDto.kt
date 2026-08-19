package com.flint.android.data.dto.collection.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionCreateResponseDto (
    @SerialName("collectionId")
    val collectionId: String
)