package com.flint.data.dto.collection.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionCreateRequestDto (
    @SerialName("imageUrl")
    val imageUrl:String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("isPublic")
    val isPublic: Boolean,
    @SerialName("contentIds")
    val contentIds: List<Long>,
)