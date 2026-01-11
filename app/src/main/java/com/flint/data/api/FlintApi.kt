package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.request.SampleRequestDto
import com.flint.data.dto.response.SampleResponseDto
import retrofit2.http.Body
import retrofit2.http.GET

interface FlintApi {
    @GET("sample")
    suspend fun sampleService(@Body requestDto: SampleRequestDto): BaseResponse<SampleResponseDto>
}
