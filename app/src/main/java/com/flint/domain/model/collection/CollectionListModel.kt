package com.flint.domain.model.collection

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


data class CollectionListModel(
    val collections: ImmutableList<CollectionItemModel> = persistentListOf()
) {
    companion object {
        val FakeList = CollectionListModel(
            collections = persistentListOf(
                CollectionItemModel(
                    id = "0",
                    thumbnailUrl = "",
                    title = "드라마 제목",
                    description = "드라마 제목 드라마 제목 드라마 제목 드라마 제목 드라마 제목",
                    imageList = emptyList(),
                    bookmarkCount = 0,
                    isBookmarked = false,
                    userId = "0",
                    nickname = "nickname",
                    profileUrl = null
                )
            )
        )
    }
}

data class CollectionItemModel(
    val id: String = "",
    val thumbnailUrl: String? = null,
    val title: String = "",
    val description: String = "",
    val imageList: List<String> = listOf(),
    val bookmarkCount: Int = 0,
    val isBookmarked: Boolean = false,
    val userId: String = "",
    val nickname: String = "",
    val profileUrl: String? = null
)
