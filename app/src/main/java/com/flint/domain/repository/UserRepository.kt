package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.UserApi
import com.flint.domain.mapper.user.toModel
import com.flint.domain.model.user.UserKeywordListModel
import javax.inject.Inject

class UserRepository
    @Inject
    constructor(
        private val apiService: UserApi,
    ) {
        suspend fun getUserKeywords(userId: Long): Result<UserKeywordListModel> =
            suspendRunCatching {
                apiService.getUserKeywords(userId).data.toModel()
            }
    }
