package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.BookmarkApi
import javax.inject.Inject

class BookmarkRepository @Inject constructor(
    private val api: BookmarkApi,
) {
    // 컬렉션 북마크 유저 조회
    suspend fun toggleCollectionBookmark(collectionId: String): Result<Boolean> =
        suspendRunCatching { api.toggleCollectionBookmark(collectionId).data }

    // 컬렉션 북마크 토글

    // 콘텐츠 북마크 토글
}
