package com.flint.domain.model.content

import com.flint.domain.type.OttType
import kotlinx.collections.immutable.persistentListOf

data class ContentModel(
    val contentId: String = "",
    val title: String = "",
    val year: Int = 0,
    val posterImage: String = "",
    val ottSimpleList: List<OttType> = emptyList(),
    val director: String = "",
    val isBookmarked: Boolean = false,
    val bookmarkCount: Int = 0,
    val description: String = "",
    val isSpoiler: Boolean = false,
    val bookmarkId: Long = 0,
    val author: String = "",
) {
    companion object {
        val FakeList =
            persistentListOf(
                ContentModel(
                    contentId = "0",
                    title = "드라마 제목",
                    year = 2000,
                    posterImage = "",
                    ottSimpleList =
                        listOf(
                            OttType.Netflix,
                            OttType.Disney,
                            OttType.Tving,
                            OttType.Coupang,
                        ),
                ),
                ContentModel(
                    contentId = "0",
                    title = "드라마 제목2",
                    year = 2020,
                    posterImage = "",
                    ottSimpleList =
                        listOf(
                            OttType.Wave,
                            OttType.Watcha,
                            OttType.Tving,
                        ),
                ),
                ContentModel(
                    contentId = "0",
                    title = "드라마 제목3",
                    year = 2003,
                    posterImage = "",
                    ottSimpleList =
                        listOf(
                            OttType.Disney,
                            OttType.Tving,
                        ),
                ),
                ContentModel(
                    contentId = "0",
                    title = "드라마 제목4",
                    year = 1919,
                    posterImage = "",
                    ottSimpleList =
                        listOf(
                            OttType.Watcha,
                        ),
                ),
            )
    }
}
