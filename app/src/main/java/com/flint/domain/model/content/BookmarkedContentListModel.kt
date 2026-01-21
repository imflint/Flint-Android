package com.flint.domain.model.content

import com.flint.domain.type.OttType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class BookmarkedContentListModel(
    val contents: ImmutableList<BookmarkedContentItemModel> = persistentListOf()
) {
    companion object {
        val FakeList = BookmarkedContentListModel(
            contents = persistentListOf(
                BookmarkedContentItemModel(
                    id = "0",
                    title = "드라마 제목",
                    year = 2000,
                    imageUrl = "",
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
    val getOttSimpleList: List<OttType> = emptyList()
)