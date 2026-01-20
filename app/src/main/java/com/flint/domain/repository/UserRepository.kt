package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.UserApi
import com.flint.domain.mapper.user.toModel
import com.flint.domain.model.user.UserKeywordResponseModel
import com.flint.domain.model.user.UserProfileResponseModel
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: UserApi,
) {
    suspend fun getUserProfile(userId: String): Result<UserProfileResponseModel> =
        suspendRunCatching {
            apiService.getUserProfile(userId).data.toModel()
        }

    suspend fun getUserKeywords(userId: String): Result<List<UserKeywordResponseModel>> =
        suspendRunCatching {
            apiService.getUserKeywords(userId).data.keywords.map {
                it.toModel()
            }
        }
}
