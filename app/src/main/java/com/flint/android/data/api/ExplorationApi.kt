package com.flint.android.data.api

import com.flint.android.data.dto.base.BaseResponse
import com.flint.android.data.dto.exploration.response.ExplorationResponseDto
import retrofit2.http.GET
import retrofit2.http.POST

interface ExplorationApi {
    @GET("/api/v1/exploration")
    suspend fun getExplorationSession(): BaseResponse<ExplorationResponseDto>

    @POST("/api/v1/exploration/next")
    suspend fun advanceToNextExplorationSession(): BaseResponse<ExplorationResponseDto>
}
