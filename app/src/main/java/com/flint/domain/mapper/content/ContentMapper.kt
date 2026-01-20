package com.flint.domain.mapper.content

import com.flint.data.dto.content.response.BookmarkedContentListResponseDto
import com.flint.data.dto.content.response.BookmarkedContentResponseDto
import com.flint.data.dto.content.response.OttSimpleResponseDto
import com.flint.data.dto.search.SearchBookmarkedContentsResponseDto
import com.flint.domain.model.content.BookmarkedContentItemModel
import com.flint.domain.model.content.BookmarkedContentListModel
import com.flint.domain.model.content.ContentModel
import com.flint.domain.type.OttType
import kotlinx.collections.immutable.toImmutableList

fun BookmarkedContentListResponseDto.toModel() : BookmarkedContentListModel {
    return BookmarkedContentListModel(
        contents = contents.map { it.toModel() }.toImmutableList()
    )
}

fun BookmarkedContentResponseDto.toModel() : BookmarkedContentItemModel {
    return BookmarkedContentItemModel(
        id = id,
        title = title,
        year = year,
        imageUrl = imageUrl,
        getOttSimpleList = getOttSimpleList.mapNotNull { ottSimple ->
            runCatching { OttType.valueOf(ottSimple.ottName) }.getOrNull()
        }
    )
}

fun SearchBookmarkedContentsResponseDto.toModel() : List<ContentModel> {
    return data.map {
        ContentModel(
            bookmarkId = it.bookmarkId,
            contentId = it.contentId,
            title = it.title,
            author = it.author,
            posterImage = it.posterUrl,
            year = it.year
        )
    }
}