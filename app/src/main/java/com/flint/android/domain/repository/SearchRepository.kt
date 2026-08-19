package com.flint.android.domain.repository

import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.data.api.SearchApi
import com.flint.android.domain.mapper.content.toModel
import com.flint.android.domain.mapper.search.toModel
import com.flint.android.domain.model.content.ContentModel
import com.flint.android.domain.model.search.SearchContentListModel
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val apiService: SearchApi,
) {
    suspend fun getBookmarkedContentList(keyword: String, cursor: Int, size: Int): Result<List<ContentModel>> =
        suspendRunCatching { apiService.getBookmarkedContentList(keyword, cursor, size).data.toModel() }

    suspend fun getSearchContentList(
        keyword: String? = null,
        genres: List<String>? = null,
        mediaType: String? = null,
        cursor: String? = null,
        size: Int = 20,
    ): Result<SearchContentListModel> =
        suspendRunCatching {
            apiService.getSearchContentList(
                keyword = keyword,
                genre = genres?.ifEmpty { null },
                mediaType = mediaType,
                cursor = cursor,
                size = size,
            ).data.toModel()
        }
}
