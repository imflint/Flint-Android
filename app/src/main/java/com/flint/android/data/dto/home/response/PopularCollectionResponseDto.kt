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
    // 토큰 없이 조회하면 서버가 isBookmarked 를 false 로 내려주지만, 누락 응답에도 안전하도록 기본값을 둔다.
    @SerialName("bookmarkCount")
    val bookmarkCount: Int = 0,
    @SerialName("isBookmarked")
    val isBookmarked: Boolean = false,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImageUrl")
    val profileUrl: String?
)
