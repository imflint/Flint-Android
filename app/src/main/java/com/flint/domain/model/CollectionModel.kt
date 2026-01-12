package com.flint.domain.model

data class CollectionModel(
    val collectionId: String,
    val collectionTitle: String,
    val collectionImageUrl: String,
    val createdAt: String,
    val isBookmarked: Boolean,
    val author: AuthorModel
)
