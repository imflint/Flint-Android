package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.CollectionApi
import com.flint.domain.mapper.collection.toModel
import com.flint.domain.model.collection.CollectionModel
import com.flint.data.dto.collection.response.CollectionDetailResponseDto
import com.flint.domain.mapper.collection.toModel
import com.flint.domain.model.collection.CollectionDetailModel
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val apiService: CollectionApi,
) {
    // 컬렉션 목록 조회 (페이지네이션)

    // 컬렉션 생성

    // 컬렉션 상세 조회
    suspend fun getCollectionDetail(collectionId: String): CollectionDetailModel {
        val response: CollectionDetailResponseDto =
            apiService.getCollectionDetail(collectionId).data

        return response.toModel()
    }

    // 최근 본 컬렉션 목록 조회
    suspend fun getRecentCollectionList() : Result<List<CollectionModel>> =
        suspendRunCatching { apiService.getRecentCollectionList().data.toModel() }
}
