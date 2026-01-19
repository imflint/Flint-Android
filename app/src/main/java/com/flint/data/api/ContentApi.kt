package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.content.response.BookmarkedContentListResponseDto
import retrofit2.http.GET

interface ContentApi {
    // 북마크한 콘텐츠 목록 조회
    @GET("api/v1/contents/bookmarks")
    suspend fun getBookmarkedContentList(): BaseResponse<BookmarkedContentListResponseDto>

    // 콘텐츠별 OTT 목록 조회

    // 콘텐츠 검색
}
