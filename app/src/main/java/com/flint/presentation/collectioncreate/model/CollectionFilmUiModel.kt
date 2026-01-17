package com.flint.presentation.collectioncreate.model

import kotlinx.collections.immutable.persistentListOf

data class CollectionFilmUiModel(
    val filmId: Long,
    val imageUrl: String,
    val title: String,
    val director: String,
    val createdYear: String,
) {
    companion object {
        val dummyFilmList =
            persistentListOf(
                CollectionFilmUiModel(
                    filmId = 1L,
                    imageUrl = "",
                    title = "인터스텔라",
                    director = "크리스토퍼 놀란",
                    createdYear = "2014",
                ),
                CollectionFilmUiModel(
                    filmId = 2L,
                    imageUrl = "",
                    title = "해리포터 불의 잔",
                    director = "마이크 뉴웰",
                    createdYear = "2005",
                ),
            )
    }
}
