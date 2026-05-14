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

    @GET("/api/v1/contents/search")
    suspend fun getSearchContentList(
        @Query("keyword") keyword: String? = null,
        @Query("genre") genre: String? = null,
        @Query("cursor") cursor: Int = 1,
        @Query("size") size: Int = 20,
    ): BaseResponse<SearchContentsResponseDto>
}
