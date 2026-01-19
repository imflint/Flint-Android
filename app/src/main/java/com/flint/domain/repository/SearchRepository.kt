package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.SearchApi
import com.flint.domain.mapper.content.toModel
import com.flint.domain.model.content.ContentModel
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val apiService: SearchApi,
) {
    suspend fun getBookmarkedContentList(keyword: String, cursor: Int, size: Int): Result<List<ContentModel>> =
        suspendRunCatching { apiService.getBookmarkedContentList(keyword, cursor, size).data.toModel() }
}
