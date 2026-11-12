package com.flint.android.data.api

import com.flint.android.data.dto.base.BaseResponse
import com.flint.android.data.dto.system.response.AppVersionResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SystemApi {
    // 플랫폼별 최소 지원 버전 조회 (강제 업데이트). 인증이 필요 없다.
    @GET("/api/v1/system/version")
    suspend fun getAppVersion(
        @Query("platform") platform: String,
    ): BaseResponse<AppVersionResponseDto>
}
