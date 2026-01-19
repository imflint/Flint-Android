package com.flint.domain.mapper.content

import com.flint.data.dto.search.BookmarkedContentsResponseDto
import com.flint.domain.model.content.ContentModel

fun BookmarkedContentsResponseDto.toModel() : List<ContentModel> {
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