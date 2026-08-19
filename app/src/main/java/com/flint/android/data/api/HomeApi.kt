package com.flint.android.data.api

import com.flint.android.data.dto.base.BaseResponse
import com.flint.android.data.dto.home.response.PopularCollectionResponseDto
import com.flint.android.data.dto.home.response.RecommendCollectionResponseDto
import retrofit2.http.GET

interface HomeApi {

    @GET("/api/v1/home/recommended-collections")
    suspend fun getRecommendedCollections(): BaseResponse<RecommendCollectionResponseDto>

    @GET("/api/v1/home/popular-collections")
    suspend fun getPopularCollections(): BaseResponse<PopularCollectionResponseDto>
}
