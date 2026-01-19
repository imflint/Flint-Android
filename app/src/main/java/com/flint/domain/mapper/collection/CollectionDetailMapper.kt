package com.flint.domain.mapper.collection

import com.flint.data.dto.collection.response.CollectionDetailResponseDto
import com.flint.domain.model.collection.CollectionDetailModel
import com.flint.domain.model.user.AuthorModel

fun CollectionDetailResponseDto.toModel(): CollectionDetailModel = CollectionDetailModel(
    collectionId = id,
    collectionTitle = title,
    collectionContent = "하이",
    collectionImageUrl1 = "TODO()",
    collectionImageUrl2 = "TODO()",
    createdAt = "TODO()",
    isBookmarked = false,
    bookmarkCount = 0,
    author = AuthorModel.Fake
)