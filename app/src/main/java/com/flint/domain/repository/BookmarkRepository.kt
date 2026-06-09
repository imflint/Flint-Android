package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.BookmarkApi
import com.flint.domain.mapper.bookmark.toModel
import com.flint.domain.model.bookmark.BookmarkChange
import com.flint.domain.model.bookmark.CollectionBookmarkUsersModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Singleton
class BookmarkRepository @Inject constructor(
    private val api: BookmarkApi,
) {
    private val _bookmarkChanges = MutableSharedFlow<BookmarkChange>()
    val bookmarkChanges = _bookmarkChanges.asSharedFlow()

    // 컬렉션 북마크 유저 조회
    suspend fun getCollectionBookmarkUsers(collectionId: String): Result<CollectionBookmarkUsersModel> {
        return suspendRunCatching { api.getCollectionBookmarkUsers(collectionId).data.toModel() }
    }

    // 컬렉션 북마크 토글
    suspend fun toggleCollectionBookmark(collectionId: String): Result<Boolean> =
        suspendRunCatching { api.toggleCollectionBookmark(collectionId).data }
            .also { result ->
                result.onSuccess { isBookmarked ->
                    _bookmarkChanges.emit(BookmarkChange.Collection(collectionId, isBookmarked))
                }
            }

    // 콘텐츠 북마크 토글
    suspend fun toggleContentBookmark(contentId: String): Result<Boolean> =
        suspendRunCatching { api.toggleContentBookmark(contentId).data }
            .also { result ->
                result.onSuccess { isBookmarked ->
                    _bookmarkChanges.emit(BookmarkChange.Content(contentId, isBookmarked))
                }
            }
}
