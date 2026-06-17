package com.flint.data.dto.content.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// /api/v1/users/{userId}/bookmarked-contents 응답 (타 유저)
@Serializable
data class BookmarkedContentListResponseDto(
    @SerialName("totalCount")
    val totalCount: Int,
    @SerialName("contents")
    val contents: List<BookmarkedContentResponseDto>
)

// /api/v1/contents/bookmarks 응답 (내 프로필, 커서 페이지네이션)
@Serializable
data class MyBookmarkedContentListResponseDto(
    @SerialName("data")
    val data: List<BookmarkedContentResponseDto>,
    @SerialName("meta")
    val meta: BookmarkCursorMetaDto
)

@Serializable
data class BookmarkCursorMetaDto(
    @SerialName("type")
    val type: String,
    @SerialName("returned")
    val returned: Int,
    @SerialName("nextCursor")
    val nextCursor: String? = null,
)

// /api/v1/contents/bookmarks/count 응답
@Serializable
data class BookmarkCountResponseDto(
    @SerialName("totalCount")
    val totalCount: Int
)

@Serializable
data class BookmarkedContentResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("year")
    val year: Int,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("bookmarkCount")
    val bookmarkCount: Int,
    // 내 북마크 목록 응답에는 isBookmarked 없음 → 기본값 true
    @SerialName("isBookmarked")
    val isBookmarked: Boolean = true,
    @SerialName("getOttSimpleList")
    val getOttSimpleList: List<OttSimpleResponseDto>
)

@Serializable
data class OttSimpleResponseDto(
    @SerialName("ottName")
    val ottName: String,
    @SerialName("logoUrl")
    val logoUrl: String
)