package com.flint.android.domain.model.content

import com.flint.android.domain.type.OttType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class BookmarkedContentListModel(
    val totalCount: Int = 0,
    val contents: ImmutableList<BookmarkedContentItemModel> = persistentListOf()
) {
    companion object {
        val FakeList = BookmarkedContentListModel(
            totalCount = 1,
            contents = persistentListOf(
                BookmarkedContentItemModel(
                    id = "0",
                    title = "드라마 제목",
                    year = 2000,
                    imageUrl = "",
                    bookmarkCount = 0,
                    getOttSimpleList = listOf(
                        OttType.Netflix,
                        OttType.Disney,
                        OttType.Tving,
                        OttType.CoupangPlay,
                    )
                )
            )
        )
    }
}

data class BookmarkedContentItemModel(
    val id: String = "",
    val title: String = "",
    val year: Int = 0,
    val imageUrl: String = "",
    val bookmarkCount: Int = 0,
    val isBookmarked: Boolean = false,
    val getOttSimpleList: List<OttType> = emptyList()
)