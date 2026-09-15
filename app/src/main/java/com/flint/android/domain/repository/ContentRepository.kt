package com.flint.android.domain.repository

import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.data.api.ContentApi
import com.flint.android.domain.mapper.content.toModel
import com.flint.android.domain.mapper.ott.toModel
import com.flint.android.domain.model.content.BookmarkedContentListModel
import com.flint.android.domain.model.ott.OttListModel
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class ContentRepository @Inject constructor(
    private val apiService: ContentApi
) {
    // 콘텐츠별 OTT 목록 조회
    suspend fun getOttListPerContent(contentId: String) : Result<OttListModel> =
        suspendRunCatching { apiService.getOttListPerContent(contentId).data.toModel() }

    // 내 북마크한 콘텐츠 목록 한 페이지 조회 (커서 페이지네이션, 무한 스크롤용)
    suspend fun getBookmarkedContentList(cursor: String?, size: Int): Result<BookmarkedContentListModel> =
        suspendRunCatching {
            val page = apiService.getBookmarkedContentList(cursor = cursor, size = size).data
            BookmarkedContentListModel(
                contents = page.data.map { it.toModel() }.toImmutableList(),
                nextCursor = page.meta.nextCursor,
            )
        }

    // 내 북마크한 콘텐츠 전체 개수 조회
    suspend fun getBookmarkedContentCount(): Result<Int> =
        suspendRunCatching { apiService.getBookmarkedContentCount().data.totalCount }

    // 콘텐츠 검색
}
