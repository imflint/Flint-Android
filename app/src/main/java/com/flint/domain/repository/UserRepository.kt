package com.flint.domain.repository

import com.flint.core.common.util.DataStoreKey.USER_ID
import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.UserApi
import com.flint.data.local.PreferencesManager
import com.flint.domain.mapper.collection.toModel
import com.flint.domain.mapper.user.toModel
import com.flint.domain.model.user.NicknameCheckModel
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.user.KeywordListModel
import com.flint.domain.model.user.UserProfileResponseModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: UserApi,
    private val preferencesManager: PreferencesManager,
) {
    private fun myUserId(): String = runBlocking {
        preferencesManager.getString(USER_ID).first()
    }

    suspend fun getUserProfile(userId: String?): Result<UserProfileResponseModel> =
        suspendRunCatching {
            apiService.getUserProfile(userId ?: myUserId()).data.toModel()
        }

    suspend fun getUserKeywords(userId: String?): Result<KeywordListModel> =
        suspendRunCatching {
            apiService.getUserKeywords(userId ?: myUserId()).data.toModel()
        }

    suspend fun getUserCreatedCollections(userId: String?): Result<CollectionListModel> =
        suspendRunCatching {
            apiService.getUserCreatedCollections(userId ?: myUserId()).data.toModel()
        }

    suspend fun getUserBookmarkedCollections(userId: String?): Result<CollectionListModel> =
        suspendRunCatching {
            apiService.getUserBookmarkedCollections(userId ?: myUserId()).data.toModel()
        }

    // 닉네임 중복 체크
    suspend fun checkNickname(nickname: String): Result<NicknameCheckModel> =
        suspendRunCatching {
            apiService.checkNickname(nickname).data.toModel()
        }
}
