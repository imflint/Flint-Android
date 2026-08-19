package com.flint.android.data.api

import com.flint.android.data.dto.auth.request.SignupRequestDto
import com.flint.android.data.dto.auth.request.SocialVerifyRequestDto
import com.flint.android.data.dto.auth.response.SignupResponseDto
import com.flint.android.data.dto.auth.response.SocialVerifyResponseDto
import com.flint.android.data.dto.auth.request.WithdrawRequestDto
import com.flint.android.data.dto.auth.response.WithdrawResponseDto
import com.flint.android.data.dto.base.BaseResponse
import retrofit2.http.Body
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

    @POST("/api/v1/auth/withdraw")
    suspend fun withdraw(
        @Body requestDto: WithdrawRequestDto,
    ): WithdrawResponseDto
}
