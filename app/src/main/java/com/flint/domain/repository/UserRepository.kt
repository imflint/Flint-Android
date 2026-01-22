package com.flint.domain.repository

import com.flint.core.common.util.DataStoreKey.USER_ID
import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.UserApi
import com.flint.data.local.PreferencesManager
import com.flint.domain.mapper.collection.toModel
import com.flint.domain.mapper.content.toModel
import com.flint.domain.mapper.user.toModel
import com.flint.domain.model.user.NicknameCheckModel
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.content.BookmarkedContentListModel
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

    // 사용자 프로필 조회
    suspend fun getUserProfile(userId: String?): Result<UserProfileResponseModel> =
        suspendRunCatching {
            apiService.getUserProfile(userId ?: myUserId()).data.toModel()
        }

    // 사용자 취향 키워드 조회
    suspend fun getUserKeywords(userId: String?): Result<KeywordListModel> =
        suspendRunCatching {
            apiService.getUserKeywords(userId ?: myUserId()).data.toModel()
        }

    // 사용자별 생성한 컬렉션 목록 조회
    suspend fun getUserCreatedCollections(userId: String?): Result<CollectionListModel> =
        suspendRunCatching {
            if (userId == null) {
                apiService.getMyCreatedCollections().data.toModel()
            } else {
                apiService.getUserCreatedCollections(userId).data.toModel()
            }
        }

    // 사용자별 북마크한 컬렉션 목록 조회
    suspend fun getUserBookmarkedCollections(userId: String?): Result<CollectionListModel> =
        suspendRunCatching {
            if (userId == null) {
                apiService.getMyBookmarkedCollections().data.toModel()
            } else {
                apiService.getUserBookmarkedCollections(userId).data.toModel()
            }
        }

    // 사용자별 북마크한 콘텐츠 목록 조회
    suspend fun getUserBookmarkedContents(userId: String?) : Result<BookmarkedContentListModel> =
        suspendRunCatching { apiService.getBookmarkedContentListByUserId(userId ?: myUserId()).data.toModel() }

    // 닉네임 중복 체크
    suspend fun checkNickname(nickname: String): Result<NicknameCheckModel> =
        suspendRunCatching {
            apiService.checkNickname(nickname).data.toModel()
        }
}
