package com.flint.android.data.dto.home.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularCollectionResponseDto(
    @SerialName("collections")
    val collections: List<PopularCollectionItemResponseDto>
)

@Serializable
data class PopularCollectionItemResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("thumbnailUrl")
    val thumbnailUrl: String?,
    @SerialName("title")
    val title: String,
    @SerialName("imageList")
    val imageList: List<String> = emptyList(),
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImageUrl")
    val profileUrl: String?
)
