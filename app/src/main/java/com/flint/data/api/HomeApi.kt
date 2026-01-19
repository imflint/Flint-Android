package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.home.response.RecommendCollectionResponseDto
import retrofit2.http.GET

interface HomeApi {

    @GET("api/v1/home/recommended-collections")
    suspend fun getRecommendedCollections() : BaseResponse<RecommendCollectionResponseDto>
}
