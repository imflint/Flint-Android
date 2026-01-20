package com.flint.domain.model.collection

data class CollectionCreateRequestModel(
    val imageUrl: String,
    val title: String,
    val description: String,
    val isPublic: Boolean,
    val contentIds: List<Long>
)