package com.flint.domain.mapper.collection

import com.flint.data.dto.collection.response.CollectionsResponseDto
import com.flint.data.dto.collection.response.RecentCollectionItemResponseDto
import com.flint.data.dto.collection.response.RecentCollectionListResponseDto
import com.flint.data.dto.home.response.RecommendCollectionItemResponseDto
import com.flint.data.dto.home.response.RecommendCollectionResponseDto
import com.flint.data.dto.user.response.BookmarkedCollectionItemResponseDto
import com.flint.data.dto.user.response.BookmarkedCollectionListResponseDto
import com.flint.data.dto.user.response.CreatedCollectionItemResponseDto
import com.flint.data.dto.user.response.CreatedCollectionListResponseDto
import com.flint.domain.model.collection.CollectionItemModel
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.collection.CollectionsModel
import kotlinx.collections.immutable.toImmutableList

fun CollectionsResponseDto.toModel(): CollectionsModel {
    return CollectionsModel(data = data.map { it.toModel() }, meta = meta.toModel())
}


fun RecommendCollectionResponseDto.toModel(): CollectionListModel {
    return CollectionListModel(
        collections = collections.map { it.toModel() }.toImmutableList()
    )
}

private fun RecommendCollectionItemResponseDto.toModel(): CollectionItemModel {
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

fun RecentCollectionListResponseDto.toModel(): CollectionListModel {
    return CollectionListModel(
        collections = collections.map { it.toModel() }.toImmutableList()
    )
}

private fun RecentCollectionItemResponseDto.toModel(): CollectionItemModel {
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

fun CreatedCollectionListResponseDto.toModel(): CollectionListModel {
    return CollectionListModel(
        collections = collections.map { it.toModel() }.toImmutableList()
    )
}

private fun CreatedCollectionItemResponseDto.toModel(): CollectionItemModel {
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

fun BookmarkedCollectionListResponseDto.toModel(): CollectionListModel {
    return CollectionListModel(
        collections = collections.map { it.toModel() }.toImmutableList()
    )
}

private fun BookmarkedCollectionItemResponseDto.toModel(): CollectionItemModel {
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

private fun CollectionsResponseDto.Collection.toModel(): CollectionsModel.Collection =
    CollectionsModel.Collection(
        collectionId = collectionId,
        createdAt = createdAt,
        description = description,
        imageUrl = imageUrl,
        title = title
    )

private fun CollectionsResponseDto.Meta.toModel(): CollectionsModel.Meta =
    CollectionsModel.Meta(
        currentPage = currentPage,
        nextCursor = nextCursor,
        returned = returned,
        totalElements = totalElements,
        totalPages = totalPages,
        type = type
    )