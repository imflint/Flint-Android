package com.flint.domain.model

data class CollectionDetailModel(
    val collectionId: String,
    val collectionTitle: String,
    val collectionContent: String,
    val collectionImageUrl1: String,
    val collectionImageUrl2: String,
    val createdAt: String,
    val isBookmarked: Boolean,
    val bookmarkCount: Int,
    val author: AuthorModel
)
