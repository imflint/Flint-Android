package com.flint.data.dto.home.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendCollectionResponseDto(
    @SerialName("collections")
    val collections: List<RecommendCollectionItemResponseDto>
)

@Serializable
data class RecommendCollectionItemResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("image")
    val image: String,
    @SerialName("profileImage")
    val profileImage: String,
    @SerialName("userName")
    val userName: String
)