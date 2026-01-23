package com.flint.domain.model.collection

import kotlinx.collections.immutable.ImmutableList

data class CollectionsModel(
    val data: ImmutableList<Collection>,
    val meta: Meta,
) {
    data class Collection(
        val collectionId: String,
        val contentDescription: String,
        val imageUrl: String,
        val contentTitle: String,
    )

    data class Meta(
        val type: String,
        val returned: Int,
        val nextCursor: Long?,
    )
}