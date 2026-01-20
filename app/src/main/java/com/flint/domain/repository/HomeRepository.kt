package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.HomeApi
import com.flint.domain.mapper.collection.toModel
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.collection.CollectionModel
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiService: HomeApi,
) {
    suspend fun getRecommendedCollectionList(): Result<CollectionListModel> =
        suspendRunCatching { apiService.getRecommendedCollections().data.toModel() }
}
