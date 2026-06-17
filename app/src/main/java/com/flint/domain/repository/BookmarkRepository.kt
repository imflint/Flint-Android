package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.BookmarkApi
import com.flint.data.dto.base.ErrorResponseDto
import com.flint.domain.mapper.bookmark.toModel
import com.flint.domain.model.bookmark.BookmarkChange
import com.flint.domain.model.bookmark.BookmarkException
import com.flint.domain.model.bookmark.CollectionBookmarkUsersModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.Json
import retrofit2.HttpException

@Singleton
class BookmarkRepository @Inject constructor(
    private val api: BookmarkApi,
    private val json: Json,
) {
    private val _bookmarkChanges = MutableSharedFlow<BookmarkChange>()
    val bookmarkChanges = _bookmarkChanges.asSharedFlow()

    // 컬렉션 북마크 유저 조회
    suspend fun getCollectionBookmarkUsers(collectionId: String): Result<CollectionBookmarkUsersModel> {
        return suspendRunCatching { api.getCollectionBookmarkUsers(collectionId).data.toModel() }
    }

    // 컬렉션 북마크 토글
    suspend fun toggleCollectionBookmark(collectionId: String): Result<Boolean> {
        val result = suspendRunCatching { api.toggleCollectionBookmark(collectionId).data }
        result.getOrNull()?.let { isBookmarked ->
            _bookmarkChanges.emit(BookmarkChange.Collection(collectionId, isBookmarked))
        }
        return result
    }

    // 콘텐츠 북마크 토글
    // 최소 저장 수 제한 초과 시 BookmarkException.ContentMinLimitExceeded 반환
    suspend fun toggleContentBookmark(contentId: String): Result<Boolean> {
        val result = suspendRunCatching {
            try {
                api.toggleContentBookmark(contentId).data
            } catch (e: HttpException) {
                val errorCode = e.response()?.errorBody()?.string()
                    ?.let { runCatching { json.decodeFromString(ErrorResponseDto.serializer(), it).errorCode }.getOrNull() }
                if (errorCode == CONTENT_MIN_LIMIT_ERROR_CODE) throw BookmarkException.ContentMinLimitExceeded
                throw e
            }
        }
        result.getOrNull()?.let { isBookmarked ->
            _bookmarkChanges.emit(BookmarkChange.Content(contentId, isBookmarked))
        }
        return result
    }

    private companion object {
        const val CONTENT_MIN_LIMIT_ERROR_CODE = "BOOKMARK.CONTENT_MIN_LIMIT"
    }
}
