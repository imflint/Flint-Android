package com.flint.domain.mapper.collection

import com.flint.data.dto.collection.response.RecentCollectionListResponseDto
import com.flint.data.dto.home.response.RecommendCollectionResponseDto
import com.flint.domain.model.collection.CollectionModel
import com.flint.domain.model.user.AuthorModel

fun RecommendCollectionResponseDto.toModel() : List<CollectionModel> {
    return collections.map {
        CollectionModel(
            collectionId = it.id,
            thumbnailUrl = it.thumbnailUrl,
            collectionTitle = it.title,
            description = it.description,
            imageList = it.imageList,
            bookmarkCount = it.bookmarkCount,
            isBookmarked = it.isBookmarked,
            author = AuthorModel(
                userId = it.userId,
                nickname = it.nickname,
                profileUrl = it.profileUrl ?: ""
            )
        )
    }
}

fun RecentCollectionListResponseDto.toModel() : List<CollectionModel> {
    return collections.map {
        CollectionModel(
            collectionId = it.id,
            thumbnailUrl = it.thumbnailUrl,
            collectionTitle = it.title,
            description = it.description,
            imageList = it.imageList,
            bookmarkCount = it.bookmarkCount,
            isBookmarked = it.isBookmarked,
            author = AuthorModel(
                userId = it.userId,
                nickname = it.nickname,
                profileUrl = it.profileUrl ?: ""
            )
        )
    }
}