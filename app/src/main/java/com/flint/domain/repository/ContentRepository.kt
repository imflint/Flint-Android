package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.ContentApi
import com.flint.domain.mapper.content.toModel
import com.flint.domain.mapper.ott.toModel
import com.flint.domain.model.content.BookmarkedContentListModel
import com.flint.domain.model.ott.OttListModel
import javax.inject.Inject

class ContentRepository @Inject constructor(
    private val apiService: ContentApi,
) {
    // 북마크한 콘텐츠 목록 조회
    suspend fun getBookmarkedContentList() : Result<BookmarkedContentListModel> =
        suspendRunCatching { apiService.getBookmarkedContentList().data.toModel() }

    // 콘텐츠별 OTT 목록 조회
    suspend fun getOttListPerContent(contentId: String) : Result<OttListModel> =
        suspendRunCatching { apiService.getOttListPerContent(contentId).data.toModel() }

    // 콘텐츠 검색
}
