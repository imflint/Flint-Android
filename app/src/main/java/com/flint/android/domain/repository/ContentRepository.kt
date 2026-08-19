package com.flint.android.domain.repository

import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.data.api.ContentApi
import com.flint.android.domain.mapper.ott.toModel
import com.flint.android.domain.model.ott.OttListModel
import javax.inject.Inject

class ContentRepository @Inject constructor(
    private val apiService: ContentApi
) {
    // 콘텐츠별 OTT 목록 조회
    suspend fun getOttListPerContent(contentId: String) : Result<OttListModel> =
        suspendRunCatching { apiService.getOttListPerContent(contentId).data.toModel() }

    // 콘텐츠 검색
}
