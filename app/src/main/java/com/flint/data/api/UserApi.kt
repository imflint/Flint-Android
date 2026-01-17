package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.user.response.UserKeywordsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("api/v1/users/{userId}/keywords")
    suspend fun getUserKeywords(
        @Path("userId") userId: Long,
    ): BaseResponse<UserKeywordsResponseDto>
}
