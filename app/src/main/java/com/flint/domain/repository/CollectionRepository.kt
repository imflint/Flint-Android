package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.CollectionApi
import com.flint.data.dto.collection.request.CollectionCreateRequestDto
import com.flint.data.dto.collection.response.CollectionDetailResponseDto
import com.flint.domain.mapper.collection.toModel
import com.flint.domain.model.collection.CollectionCreateModel
import com.flint.domain.model.collection.CollectionDetailModelNew
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.collection.CollectionsModel
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val apiService: CollectionApi,
) {
    // 컬렉션 목록 조회 (페이지네이션)
    suspend fun getCollections(cursor: Long?, size: Int): Result<CollectionsModel> =
        suspendRunCatching {
            apiService.getCollections(
                cursor = cursor,
                size = size
            ).data.toModel()
        }

    // 컬렉션 생성
    suspend fun postCollectionCreate(
        requestDto: CollectionCreateRequestDto,
    ): Result<CollectionCreateModel> =
        suspendRunCatching {
            apiService.postCollectionCreate(requestDto).data.toModel()
        }


    // 컬렉션 상세 조회
    suspend fun getCollectionDetail(collectionId: String): Result<CollectionDetailModelNew> =
        suspendRunCatching {
            val response: CollectionDetailResponseDto =
                apiService.getCollectionDetail(collectionId).data

            response.toModel()
        }

    // 최근 본 컬렉션 목록 조회
    suspend fun getRecentCollectionList(): Result<CollectionListModel> =
        suspendRunCatching { apiService.getRecentCollectionList().data.toModel() }
}
