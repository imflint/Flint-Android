package com.flint.data.dto.collection.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecentCollectionListResponseDto(
    @SerialName("collections")
    val collections: List<RecentCollectionItemResponseDto>
)

@Serializable
data class RecentCollectionItemResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("thumbnailUrl")
    val thumbnailUrl: String?,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("imageList")
    val imageList: List<String>,
    @SerialName("bookmarkCount")
    val bookmarkCount: Int,
    @SerialName("isBookmarked")
    val isBookmarked: Boolean,
    @SerialName("userId")
    val userId: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImageUrl")
    val profileUrl: String?
)