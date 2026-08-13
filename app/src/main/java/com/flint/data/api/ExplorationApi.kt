package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.exploration.response.ExplorationResponseDto
import retrofit2.http.GET
import retrofit2.http.POST

interface ExplorationApi {
    @GET("/api/v1/exploration")
    suspend fun getExplorationSession(): BaseResponse<ExplorationResponseDto>

    @POST("/api/v1/exploration/next")
    suspend fun advanceToNextExplorationSession(): BaseResponse<ExplorationResponseDto>
}
