package com.flint.data.model.collection

import com.flint.data.model.user.AuthorModel

data class CollectionDetailModel(
    val collectionId: String,
    val collectionTitle: String,
    val collectionContent: String,
    val collectionImageUrl1: String,
    val collectionImageUrl2: String,
    val createdAt: String,
    val isBookmarked: Boolean,
    val bookmarkCount: Int,
    val author: AuthorModel,
) {
    companion object {
        val Fake =
            CollectionDetailModel(
                collectionId = "id_0",
                collectionTitle = "컬렉션 제목 1",
                collectionContent = "컬렉션에 대한 설명이 들어갑니다. 최대 2줄까지 표시됩니다.",
                collectionImageUrl1 = "",
                collectionImageUrl2 = "",
                createdAt = "2024-01-01",
                isBookmarked = true,
                bookmarkCount = 10,
                author = AuthorModel.Companion.Fake,
            )
    }
}