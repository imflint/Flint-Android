package com.flint.domain.mapper.collection

import com.flint.data.dto.home.response.RecommendCollectionResponseDto
import com.flint.domain.model.collection.CollectionModel
import com.flint.domain.model.user.AuthorModel

fun RecommendCollectionResponseDto.toModel() : List<CollectionModel> {
    return collections.map {
        CollectionModel(
            collectionId = it.id,
            collectionTitle = it.title,
            collectionImageUrl = it.image,
            author = AuthorModel(
                nickname = it.userName,
                profileUrl = it.profileImage
            )
        )
    }
}