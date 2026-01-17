package com.flint.data.repository

import com.flint.data.model.user.UserKeywordListModel

interface UserRepository {
    suspend fun getUserKeywords(userId: Long): Result<UserKeywordListModel>
}
