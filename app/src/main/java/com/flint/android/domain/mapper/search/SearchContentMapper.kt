package com.flint.android.domain.mapper.search

import com.flint.android.data.dto.search.SearchContentsResponseDto
import com.flint.android.domain.model.search.SearchContentItemModel
import com.flint.android.domain.model.search.SearchContentListModel
import kotlinx.collections.immutable.toImmutableList

fun SearchContentsResponseDto.toModel(): SearchContentListModel {
    return SearchContentListModel(
        contents = this.data.map { it.toModel() }.toImmutableList(),
        nextCursor = this.meta?.nextCursor,
    )
}

private fun SearchContentsResponseDto.Content.toModel(): SearchContentItemModel{
    return SearchContentItemModel(
        id = id,
        title = title,
        author = author,
        posterUrl = posterUrl,
        year = year
    )
}



