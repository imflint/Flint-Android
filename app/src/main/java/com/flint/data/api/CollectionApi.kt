package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.collection.response.CollectionDetailResponseDto
import com.flint.data.dto.collection.response.RecentCollectionListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CollectionApi {
    // 컬렉션 목록 조회 (페이지네이션)

    // 컬렉션 생성

    // 컬렉션 상세 조회
    @GET("api/v1/collections/{collectionId}")
    suspend fun getCollectionDetail(
        @Path("collectionId") collectionId: String,
    ): BaseResponse<CollectionDetailResponseDto>

    // 최근 본 컬렉션 목록 조회
    @GET("/api/v1/collections/recent")
    suspend fun getRecentCollectionList(): BaseResponse<RecentCollectionListResponseDto>
}
