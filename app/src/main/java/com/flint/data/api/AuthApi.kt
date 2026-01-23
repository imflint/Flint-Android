package com.flint.data.api

import com.flint.data.dto.auth.request.SignupRequestDto
import com.flint.data.dto.auth.request.SocialVerifyRequestDto
import com.flint.data.dto.auth.response.SignupResponseDto
import com.flint.data.dto.auth.response.SocialVerifyResponseDto
import com.flint.data.dto.auth.response.WithdrawResponseDto
import com.flint.data.dto.base.BaseResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v1/auth/signup")
    suspend fun signup(
        @Body requestDto: SignupRequestDto,
    ): BaseResponse<SignupResponseDto>

    @POST("/api/v1/auth/social/verify")
    suspend fun socialVerify(
        @Body requestDto: SocialVerifyRequestDto,
    ): BaseResponse<SocialVerifyResponseDto>

    @DELETE("/api/v1/auth/withdraw")
    suspend fun withdraw(): WithdrawResponseDto
}
