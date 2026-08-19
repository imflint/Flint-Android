package com.flint.android.domain.repository

import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.data.api.HomeApi
import com.flint.android.domain.mapper.collection.toModel
import com.flint.android.domain.model.collection.CollectionListModel
import com.flint.android.domain.model.collection.CollectionModel
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiService: HomeApi,
) {
    suspend fun getRecommendedCollectionList(): Result<CollectionListModel> =
        suspendRunCatching { apiService.getRecommendedCollections().data.toModel() }

    suspend fun getPopularCollectionList(): Result<CollectionListModel> =
        suspendRunCatching { apiService.getPopularCollections().data.toModel() }
}
