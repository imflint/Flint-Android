package com.flint.domain.mapper.collection

import com.flint.data.dto.collection.response.RecentCollectionItemResponseDto
import com.flint.data.dto.collection.response.RecentCollectionListResponseDto
import com.flint.data.dto.home.response.RecommendCollectionItemResponseDto
import com.flint.data.dto.home.response.RecommendCollectionResponseDto
import com.flint.data.dto.user.response.CreatedCollectionItemResponseDto
import com.flint.data.dto.user.response.CreatedCollectionListResponseDto
import com.flint.domain.model.collection.CollectionItemModel
import com.flint.domain.model.collection.CollectionListModel
import kotlinx.collections.immutable.toImmutableList

fun RecommendCollectionResponseDto.toModel() : CollectionListModel {
    return CollectionListModel(
        collections = collections.map { it.toModel() }.toImmutableList()
    )
}

private fun RecommendCollectionItemResponseDto.toModel() : CollectionItemModel {
    return CollectionItemModel(
        id = id,
        thumbnailUrl = thumbnailUrl,
        title = title,
        description = description,
        imageList = imageList,
        bookmarkCount = bookmarkCount,
        isBookmarked = isBookmarked,
        userId = userId,
        nickname = nickname,
        profileUrl = profileUrl
    )
}

fun RecentCollectionListResponseDto.toModel() : CollectionListModel {
    return CollectionListModel(
        collections = collections.map { it.toModel() }.toImmutableList()
    )
}

private fun RecentCollectionItemResponseDto.toModel() : CollectionItemModel {
    return CollectionItemModel(
        id = id,
        thumbnailUrl = thumbnailUrl,
        title = title,
        description = description,
        imageList = imageList,
        bookmarkCount = bookmarkCount,
        isBookmarked = isBookmarked,
        userId = userId,
        nickname = nickname,
        profileUrl = profileUrl
    )
}

fun CreatedCollectionListResponseDto.toModel() : CollectionListModel {
    return CollectionListModel(
        collections = collections.map { it.toModel() }.toImmutableList()
    )
}

private fun CreatedCollectionItemResponseDto.toModel() : CollectionItemModel {
    return CollectionItemModel(
        id = id,
        thumbnailUrl = thumbnailUrl,
        title = title,
        description = description,
        imageList = imageList,
        bookmarkCount = bookmarkCount,
        isBookmarked = isBookmarked,
        userId = userId,
        nickname = nickname,
        profileUrl = profileUrl
    )
}