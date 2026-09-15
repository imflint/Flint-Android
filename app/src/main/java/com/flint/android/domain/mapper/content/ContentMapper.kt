package com.flint.android.domain.mapper.content

import com.flint.android.data.dto.content.response.BookmarkedContentListResponseDto
import com.flint.android.data.dto.content.response.BookmarkedContentResponseDto
import com.flint.android.data.dto.content.response.OttSimpleResponseDto
import com.flint.android.domain.model.content.BookmarkedContentItemModel
import com.flint.android.domain.model.content.BookmarkedContentListModel
import com.flint.android.domain.type.OttType
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber

// /api/v1/users/{userId}/bookmarked-contents (타 유저)
fun BookmarkedContentListResponseDto.toModel() : BookmarkedContentListModel {
    return BookmarkedContentListModel(
        totalCount = totalCount,
        contents = contents.map { it.toModel() }.toImmutableList()
    )
}

fun BookmarkedContentResponseDto.toModel() : BookmarkedContentItemModel {
    return BookmarkedContentItemModel(
        id = id,
        title = title,
        author = author,
        year = year,
        imageUrl = imageUrl,
        bookmarkCount = bookmarkCount,
        isBookmarked = isBookmarked,
        getOttSimpleList = getOttSimpleList.mapNotNull { ottSimple ->
            runCatching { OttType.valueOf(ottSimple.ottName) }
                .onFailure { Timber.w("Unknown OTT name from server: ${ottSimple.ottName}") }
                .getOrNull()
        }
    )
}
