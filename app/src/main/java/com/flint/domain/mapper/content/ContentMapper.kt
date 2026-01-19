package com.flint.domain.mapper.content

import com.flint.data.dto.content.response.BookmarkedContentListResponseDto
import com.flint.data.dto.content.response.OttSimpleResponseDto
import com.flint.data.dto.search.SearchBookmarkedContentsResponseDto
import com.flint.domain.model.content.ContentModel
import com.flint.domain.type.OttType

fun BookmarkedContentListResponseDto.toModel() : List<ContentModel> {
    return contents.map {
        ContentModel(
            contentId = it.id,
            title = it.title,
            year = it.year,
            ottSimpleList = it.getOttSimpleList.mapNotNull { ottSimple ->
                runCatching { OttType.valueOf(ottSimple.ottName) }.getOrNull()
            }
        )
    }
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