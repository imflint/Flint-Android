package com.flint.domain.repository

import com.flint.core.common.util.DataStoreKey.USER_ID
import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.CollectionApi
import com.flint.data.dto.collection.request.CollectionCreateRequestDto
import com.flint.data.dto.collection.response.CollectionDetailResponseDto
import com.flint.data.local.PreferencesManager
import com.flint.domain.mapper.collection.toModel
import com.flint.domain.model.collection.CollectionDetailModelNew
import com.flint.domain.model.collection.CollectionListModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val apiService: CollectionApi,
    private val preferencesManager: PreferencesManager,
) {
    // 컬렉션 목록 조회 (페이지네이션)

    // 컬렉션 생성
    suspend fun postCollectionCreate(
        requestDto: CollectionCreateRequestDto
    ): Result<Unit> =
        suspendRunCatching {
            apiService.postCollectionCreate(requestDto)
            Unit
        }


    // 컬렉션 상세 조회
    suspend fun getCollectionDetail(collectionId: String): Result<CollectionDetailModelNew> =
        suspendRunCatching {
            val response: CollectionDetailResponseDto =
                apiService.getCollectionDetail(collectionId).data
            val userId: String = preferencesManager.getString(USER_ID).first()

            CollectionDetailModelNew(response, userId)
        }

    // 최근 본 컬렉션 목록 조회
    suspend fun getRecentCollectionList(): Result<CollectionListModel> =
        suspendRunCatching { apiService.getRecentCollectionList().data.toModel() }
}
