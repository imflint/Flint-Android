package com.flint.domain.mapper.search

import com.flint.data.dto.search.SearchContentsResponseDto
import com.flint.domain.model.search.SearchContentItemModel
import com.flint.domain.model.search.SearchContentListModel
import kotlinx.collections.immutable.toImmutableList

fun SearchContentsResponseDto.toModel(): SearchContentListModel {
    return SearchContentListModel(
        contents = this.contents.map { it.toModel() }.toImmutableList()
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



