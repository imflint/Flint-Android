package com.flint.android.domain.mapper.search

import com.flint.android.data.dto.search.SearchContentsResponseDto
import com.flint.android.domain.model.search.SearchContentItemModel
import com.flint.android.domain.model.search.SearchContentListModel
import kotlinx.collections.immutable.toImmutableList

fun SearchContentsResponseDto.toModel(): SearchContentListModel {
    return SearchContentListModel(
        // id 없는 항목은 LazyGrid의 key로 쓸 수 없어 식별 불가능하므로 제외
        contents = this.data.mapNotNull { it.toModel() }.toImmutableList(),
        nextCursor = this.meta?.nextCursor,
    )
}

private fun SearchContentsResponseDto.Content.toModel(): SearchContentItemModel? {
    val id = id ?: return null
    return SearchContentItemModel(
        id = id,
        title = title.orEmpty(),
        author = author.orEmpty(),
        posterUrl = posterUrl.orEmpty(),
        year = year ?: 0,
    )
}



