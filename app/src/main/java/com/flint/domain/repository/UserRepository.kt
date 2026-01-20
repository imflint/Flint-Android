package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.UserApi
import com.flint.domain.mapper.collection.toModel
import com.flint.domain.mapper.user.toModel
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.user.KeywordListModel
import com.flint.domain.model.user.UserProfileResponseModel
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: UserApi,
) {
    private val myTempUserId = "801159854933808613" //TODO: 토큰 userId

    suspend fun getUserProfile(userId: String?): Result<UserProfileResponseModel> =
        suspendRunCatching {
            apiService.getUserProfile(userId ?: myTempUserId).data.toModel()
        }

    suspend fun getUserKeywords(userId: String?): Result<KeywordListModel> =
        suspendRunCatching {
            apiService.getUserKeywords(userId ?: myTempUserId).data.toModel()
        }

    suspend fun getUserCreatedCollections(userId: String?): Result<CollectionListModel> =
        suspendRunCatching {
            apiService.getUserCreatedCollections(userId ?: myTempUserId).data.toModel()
        }

    suspend fun getUserBookmarkedCollections(userId: String?): Result<CollectionListModel> =
        suspendRunCatching {
            apiService.getUserBookmarkedCollections(userId ?: myTempUserId).data.toModel()
        }
}
