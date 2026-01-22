package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.collection.request.CollectionCreateRequestDto
import com.flint.data.dto.collection.response.CollectionCreateResponseDto
import com.flint.data.dto.collection.response.CollectionDetailResponseDto
import com.flint.data.dto.collection.response.CollectionsResponseDto
import com.flint.data.dto.collection.response.RecentCollectionListResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CollectionApi {
    // 컬렉션 목록 조회 (페이지네이션)
    @GET("/api/v1/collections")
    suspend fun getCollections(
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int,
    ): BaseResponse<CollectionsResponseDto>

    // 컬렉션 생성
    @POST("/api/v1/collections")
    suspend fun postCollectionCreate(
        @Body requestDto: CollectionCreateRequestDto,
    ): BaseResponse<CollectionCreateResponseDto>

    // 컬렉션 상세 조회
    @GET("/api/v1/collections/{collectionId}")
    suspend fun getCollectionDetail(
        @Path("collectionId") collectionId: String,
    ): BaseResponse<CollectionDetailResponseDto>

    // 최근 본 컬렉션 목록 조회
    @GET("/api/v1/collections/recent")
    suspend fun getRecentCollectionList(): BaseResponse<RecentCollectionListResponseDto>
}
