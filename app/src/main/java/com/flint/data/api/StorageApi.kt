package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.storage.response.PresignedUrlResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface StorageApi {
    // Presigned URL 발급
    @GET("/api/v1/storage/presigned-url")
    suspend fun getPresignedUrl(
        @Query("pathType") pathType: String,
        @Query("extension") extension: String,
    ): BaseResponse<PresignedUrlResponseDto>
}
