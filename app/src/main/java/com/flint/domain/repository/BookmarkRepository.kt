package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.BookmarkApi
import com.flint.domain.mapper.bookmark.toModel
import com.flint.domain.model.bookmark.CollectionBookmarkUsersModel
import javax.inject.Inject

class BookmarkRepository @Inject constructor(
    private val api: BookmarkApi,
) {
    // 컬렉션 북마크 유저 조회
    suspend fun getCollectionBookmarkUsers(collectionId: String): Result<CollectionBookmarkUsersModel> {
        return suspendRunCatching { api.getCollectionBookmarkUsers(collectionId).data.toModel() }
    }

    // 컬렉션 북마크 토글
    suspend fun toggleCollectionBookmark(collectionId: String): Result<Boolean> =
        suspendRunCatching { api.toggleCollectionBookmark(collectionId).data }

    // 콘텐츠 북마크 토글
    suspend fun toggleContentBookmark(contentId: String): Result<Boolean> =
        suspendRunCatching { api.toggleContentBookmark(contentId).data }
}
