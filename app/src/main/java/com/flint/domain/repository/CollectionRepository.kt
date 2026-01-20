package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.CollectionApi
import com.flint.domain.mapper.collection.toModel
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.collection.CollectionModel
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val apiService: CollectionApi,
) {
    // 컬렉션 목록 조회 (페이지네이션)

    // 컬렉션 생성

    // 컬렉션 상세 조회

    // 최근 본 컬렉션 목록 조회
    suspend fun getRecentCollectionList() : Result<CollectionListModel> =
        suspendRunCatching { apiService.getRecentCollectionList().data.toModel() }
}
