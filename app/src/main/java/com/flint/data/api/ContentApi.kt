package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.content.response.MyBookmarkedContentListResponseDto
import com.flint.data.dto.ott.response.OttListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ContentApi {
    // 내 북마크한 콘텐츠 목록 조회 (커서 페이지네이션)
    @GET("/api/v1/contents/bookmarks")
    suspend fun getBookmarkedContentList(
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int = 10,
    ): BaseResponse<MyBookmarkedContentListResponseDto>

    // 콘텐츠별 OTT 목록 조회
    @GET("/api/v1/contents/ott/{contentId}")
    suspend fun getOttListPerContent(
        @Path("contentId") contentId: String
    ): BaseResponse<OttListResponseDto>

    // 콘텐츠 검색
}
