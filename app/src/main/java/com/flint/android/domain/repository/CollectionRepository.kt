package com.flint.android.domain.repository

import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.data.api.CollectionApi
import com.flint.android.data.dto.collection.request.CollectionCreateRequestDto
import com.flint.android.data.dto.collection.response.CollectionDetailResponseDto
import com.flint.android.domain.mapper.collection.toDto
import com.flint.android.domain.mapper.collection.toModel
import com.flint.android.domain.model.collection.CollectionCreateModel
import com.flint.android.domain.model.collection.CollectionDetailModelNew
import com.flint.android.domain.model.collection.CollectionListModel
import com.flint.android.domain.model.collection.CollectionReportRequestModel
import com.flint.android.domain.model.collection.CollectionsModel
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

    // 컬렉션 수정
    suspend fun updateCollection(
        collectionId: String,
        requestDto: CollectionCreateRequestDto,
    ): Result<Unit> =
        suspendRunCatching {
            apiService.updateCollection(collectionId, requestDto)
            Unit
        }

    // 컬렉션 삭제
    suspend fun deleteCollection(collectionId: String): Result<Unit> =
        suspendRunCatching {
            apiService.deleteCollection(collectionId)
            Unit
        }

    // 최근 본 컬렉션 목록 조회
    suspend fun getRecentCollectionList(): Result<CollectionListModel> =
        suspendRunCatching { apiService.getRecentCollectionList().data.toModel() }

    // 컬렉션 신고
    suspend fun postCollectionReport(
        collectionId: String,
        requestModel: CollectionReportRequestModel,
    ): Result<Unit> =
        suspendRunCatching {
            apiService.postCollectionReport(collectionId, requestModel.toDto())
        }.map {}
}
