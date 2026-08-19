package com.flint.android.domain.repository

import com.flint.android.core.common.util.DataStoreKey.USER_ID
import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.data.api.ContentApi
import com.flint.android.data.api.UserApi
import com.flint.android.data.dto.user.request.UpdateNicknameRequestDto
import com.flint.android.data.dto.user.request.UpdateProfileImageRequestDto
import com.flint.android.data.local.PreferencesManager
import com.flint.android.domain.mapper.collection.toModel
import com.flint.android.domain.mapper.content.toModel
import com.flint.android.domain.mapper.user.toModel
import com.flint.android.domain.model.collection.CollectionListModel
import com.flint.android.domain.model.content.BookmarkedContentItemModel
import com.flint.android.domain.model.content.BookmarkedContentListModel
import com.flint.android.domain.model.user.KeywordListModel
import com.flint.android.domain.model.user.NicknameCheckModel
import com.flint.android.domain.model.user.UserProfileResponseModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: UserApi,
    private val contentApi: ContentApi,
    private val preferencesManager: PreferencesManager,
) {
    private suspend fun myUserId(): String {
        return preferencesManager.getString(USER_ID).first()
    }

    // 사용자 프로필 조회 (내 프로필, 타 유저)
    suspend fun getUserProfile(userId: String?): Result<UserProfileResponseModel> =
        suspendRunCatching {
            if (userId == null) {
                apiService.getMyProfile().data.toModel()
            } else {
                apiService.getUserProfile(userId).data.toModel()
            }
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
    // - 내 프로필 (userId == null): GET /api/v1/contents/bookmarks (커서 페이지네이션 전체 로드)
    //                              + GET /api/v1/contents/bookmarks/count (totalCount 별도 조회)
    // - 타 유저 (userId != null): GET /api/v1/users/{userId}/bookmarked-contents
    suspend fun getUserBookmarkedContents(userId: String?): Result<BookmarkedContentListModel> =
        suspendRunCatching {
            if (userId == null) {
                val totalCount = contentApi.getBookmarkedContentCount().data.totalCount
                val allContents = mutableListOf<BookmarkedContentItemModel>()
                var cursor: String? = null
                do {
                    val page = contentApi.getBookmarkedContentList(cursor = cursor).data
                    allContents.addAll(page.data.map { it.toModel() })
                    cursor = page.meta.nextCursor
                } while (cursor != null)
                BookmarkedContentListModel(
                    totalCount = totalCount,
                    contents = allContents.toImmutableList()
                )
            } else {
                apiService.getBookmarkedContentListByUserId(userId).data.toModel()
            }
        }

    // 취향 키워드 재계산
    suspend fun recalculateKeywords(): Result<Unit> =
        suspendRunCatching { apiService.recalculateKeywords().also { check(it.isSuccessful) } }

    // 닉네임 중복 체크
    suspend fun checkNickname(nickname: String): Result<NicknameCheckModel> =
        suspendRunCatching {
            apiService.checkNickname(nickname).data.toModel()
        }

    // 닉네임 수정
    suspend fun updateNickname(nickname: String): Result<Unit> =
        suspendRunCatching {
            apiService.updateNickname(UpdateNicknameRequestDto(nickname = nickname))
            Unit
        }

    // 프로필 이미지 수정
    suspend fun updateProfileImage(imageKey: String): Result<Unit> =
        suspendRunCatching {
            apiService.updateProfileImage(UpdateProfileImageRequestDto(profileImage = imageKey))
            Unit
        }
}
