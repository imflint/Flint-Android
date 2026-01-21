package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.user.response.NicknameCheckResponseDto
import com.flint.data.dto.user.response.BookmarkedCollectionListResponseDto
import com.flint.data.dto.user.response.CreatedCollectionListResponseDto
import com.flint.data.dto.user.response.UserKeywordsResponseDto
import com.flint.data.dto.user.response.UserProfileResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    // 사용자 프로필 조회
    @GET("/api/v1/users/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: String,
    ): BaseResponse<UserProfileResponseDto>

    // 닉네임 중복 체크
    @GET("/api/v1/users/nickname/check")
    suspend fun checkNickname(
        @Query("nickname") nickname: String
    ): BaseResponse<NicknameCheckResponseDto>

    // 사용자 북마크 컬렉션 조회
    @GET("/api/v1/users/{userId}/bookmarked-collections")
    suspend fun getUserBookmarkedCollections(
        @Path("userId") userId: String,
    ): BaseResponse<BookmarkedCollectionListResponseDto>

    // 사용자 생성 컬렉션 조회
    @GET("/api/v1/users/{userId}/collections")
    suspend fun getUserCreatedCollections(
        @Path("userId") userId: String,
    ): BaseResponse<CreatedCollectionListResponseDto>

    // 사용자 취향 키워드 조회
    @GET("/api/v1/users/{userId}/keywords")
    suspend fun getUserKeywords(
        @Path("userId") userId: String,
    ): BaseResponse<UserKeywordsResponseDto>

    // 취향 키워드 재계산
}
