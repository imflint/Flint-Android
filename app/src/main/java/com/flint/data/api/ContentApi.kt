package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.content.response.BookmarkedContentListResponseDto
import com.flint.data.dto.ott.response.OttListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ContentApi {
    // 북마크한 콘텐츠 목록 조회
    @GET("/api/v1/contents/bookmarks")
    suspend fun getBookmarkedContentList(): BaseResponse<BookmarkedContentListResponseDto>

    // 사용자별 북마크한 콘텐츠 목록 조회
    @GET("/api/v1/contents/{userId}/bookmarked-contents")
    suspend fun getBookmarkedContentListByUserId(
        @Path("userId") userId: String
    ): BaseResponse<BookmarkedContentListResponseDto>

    // 콘텐츠별 OTT 목록 조회
    @GET("/api/v1/contents/ott/{contentId}")
    suspend fun getOttListPerContent(
        @Path("contentId") contentId: String
    ): BaseResponse<OttListResponseDto>

    // 콘텐츠 검색
}
