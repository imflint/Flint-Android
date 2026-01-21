package com.flint.domain.model.collection

data class CollectionsModel(
    val data: List<Collection>,
    val meta: Meta,
) {
    data class Collection(
        val collectionId: String,
        val createdAt: String,
        val description: String,
        val imageUrl: String,
        val title: String,
    )

    data class Meta(
        val currentPage: Int,
        val nextCursor: String,
        val returned: Int,
        val totalElements: Int,
        val totalPages: Int,
        val type: String,
    )
}