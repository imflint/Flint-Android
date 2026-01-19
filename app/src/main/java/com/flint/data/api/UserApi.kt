package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.user.response.UserKeywordsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    // 사용자 프로필 조회

    // 닉네임 중복 체크

    // 사용자 북마크 컬렉션 조회

    // 사용자 생성 컬렉션 조회

    // 사용자 취향 키워드 조회
    @GET("/api/v1/users/{userId}/keywords")
    suspend fun getUserKeywords(
        @Path("userId") userId: Long,
    ): BaseResponse<UserKeywordsResponseDto>

    // 취향 키워드 재계산
}
