package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.search.BookmarkedContentsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("api/v1/search/bookmarked-contents")
    suspend fun getBookmarkedContentList(
        @Query("keyword") keyword: String,
        @Query("cursor") cursor: Int,
        @Query("size") size: Int
    ) : BaseResponse<BookmarkedContentsResponseDto>
}
