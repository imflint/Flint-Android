package com.flint.domain.model.collection

data class CollectionCreateRequestModel(
    val imageUrl: String,
    val title: String,
    val description: String,
    val isPublic: Boolean,
    val contentList: List<CollectionCreateContentModel>,
)

data class CollectionCreateContentModel(
    val contentId: String,
    val isSpoiler: Boolean,
    val reason: String,
)
