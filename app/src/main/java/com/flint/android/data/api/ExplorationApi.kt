package com.flint.android.data.api

import com.flint.android.data.dto.base.BaseResponse
import com.flint.android.data.dto.exploration.request.ExplorationProgressRequestDto
import com.flint.android.data.dto.exploration.response.ExplorationResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ExplorationApi {
    @GET("/api/v1/exploration")
    suspend fun getExplorationSession(): BaseResponse<ExplorationResponseDto>

    @PATCH("/api/v1/exploration/progress")
    suspend fun updateExplorationProgress(
        @Body requestDto: ExplorationProgressRequestDto,
    ): BaseResponse<ExplorationResponseDto>
}
