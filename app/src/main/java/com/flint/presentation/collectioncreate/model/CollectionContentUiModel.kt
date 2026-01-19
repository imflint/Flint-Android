package com.flint.presentation.collectioncreate.model

import kotlinx.collections.immutable.persistentListOf

//TODO: UiModel 삭제 필요
data class CollectionContentUiModel(
    val contentId: Long,
    val imageUrl: String,
    val title: String,
    val director: String,
    val createdYear: String,
) {
    companion object {
        val dummyContentList =
            persistentListOf(
                CollectionContentUiModel(
                    contentId = 1L,
                    imageUrl = "",
                    title = "인터스텔라",
                    director = "크리스토퍼 놀란",
                    createdYear = "2014",
                ),
                CollectionContentUiModel(
                    contentId = 2L,
                    imageUrl = "",
                    title = "해리포터 불의 잔",
                    director = "마이크 뉴웰",
                    createdYear = "2005",
                ),
            )
    }
}
