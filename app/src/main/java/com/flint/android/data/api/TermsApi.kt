package com.flint.android.data.api

import com.flint.android.data.dto.base.BaseResponse
import com.flint.android.data.dto.terms.response.TermResponseDto
import com.flint.android.data.dto.terms.response.TermsListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TermsApi {
    @GET("/api/v1/terms")
    suspend fun getTermsList(
        @Query("type") type: String? = null,
    ): BaseResponse<TermsListResponseDto>

    @GET("/api/v1/terms/{termsId}")
    suspend fun getTermsDetail(
        @Path("termsId") termsId: Long,
    ): BaseResponse<TermResponseDto>
}
