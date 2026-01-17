package com.flint.data.repositoryImpl

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.UserApi
import com.flint.data.mapper.user.toModel
import com.flint.data.model.user.UserKeywordListModel
import com.flint.data.repository.UserRepository
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val apiService: UserApi,
) : UserRepository {

    override suspend fun getUserKeywords(userId: Long): Result<UserKeywordListModel> =
        suspendRunCatching {
            apiService.getUserKeywords(userId).data.toModel()
        }
}