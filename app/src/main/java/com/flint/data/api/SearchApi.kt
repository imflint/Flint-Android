package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.search.SearchBookmarkedContentsResponseDto
import com.flint.data.dto.search.SearchContentsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("/api/v1/search/bookmarked-contents")
    suspend fun getBookmarkedContentList(
        @Query("keyword") keyword: String,
        @Query("cursor") cursor: Int,
        @Query("size") size: Int
    ) : BaseResponse<SearchBookmarkedContentsResponseDto>

    @GET("/api/v1/search/contents")
    suspend fun getSearchContentList(
        @Query("keyword") keyword: String?
    ): BaseResponse<SearchContentsResponseDto>
}
