package com.flint.domain.model.search

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SearchContentItemModel(
    val id: String,
    val title: String,
    val author: String,
    val posterUrl: String,
    val year: Int,
)

data class SearchContentListModel(
    val contents: ImmutableList<SearchContentItemModel>
) {
    companion object {
        val FakeList =
            persistentListOf(
                SearchContentItemModel(
                    id = "1",
                    title = "은하수를 여행하는 히치하이커를 위한 안내서",
                    author = "가스 제닝스",
                    posterUrl = "https://image.tmdb.org/t/p/w500/ib6v6qUXzez1x2qIOLN7C0yJNPQ.jpg",
                    year = 2005,
                ),
                SearchContentItemModel(
                    id = "2",
                    title = "인터스텔라",
                    author = "크리스토퍼 놀란",
                    posterUrl = "https://image.tmdb.org/t/p/w500/ib6v6qUXzez1x2qIOLN7C0yJNPQ.jpg",
                    year = 2014,
                ),
                SearchContentItemModel(
                    id = "3",
                    title = "인셉션",
                    author = "크리스토퍼 놀란",
                    posterUrl = "https://image.tmdb.org/t/p/w500/ib6v6qUXzez1x2qIOLN7C0yJNPQ.jpg",
                    year = 2010,
                ),
                SearchContentItemModel(
                    id = "4",
                    title = "기생충",
                    author = "봉준호",
                    posterUrl = "https://image.tmdb.org/t/p/w500/ib6v6qUXzez1x2qIOLN7C0yJNPQ.jpg",
                    year = 2019,
                ),
                SearchContentItemModel(
                    id = "5",
                    title = "어벤져스: 엔드게임",
                    author = "안소니 루소",
                    posterUrl = "https://image.tmdb.org/t/p/w500/ib6v6qUXzez1x2qIOLN7C0yJNPQ.jpg",
                    year = 2019,
                ),
                SearchContentItemModel(
                    id = "6",
                    title = "다크 나이트",
                    author = "크리스토퍼 놀란",
                    posterUrl = "https://image.tmdb.org/t/p/w500/ib6v6qUXzez1x2qIOLN7C0yJNPQ.jpg",
                    year = 2008,
                ),
                SearchContentItemModel(
                    id = "7",
                    title = "오펜하이머",
                    author = "크리스토퍼 놀란",
                    posterUrl = "https://image.tmdb.org/t/p/w500/ib6v6qUXzez1x2qIOLN7C0yJNPQ.jpg",
                    year = 2023,
                ),
                SearchContentItemModel(
                    id = "8",
                    title = "괴물",
                    author = "봉준호",
                    posterUrl = "https://image.tmdb.org/t/p/w500/ib6v6qUXzez1x2qIOLN7C0yJNPQ.jpg",
                    year = 2006,
                ),
                SearchContentItemModel(
                    id = "9",
                    title = "설국열차",
                    author = "봉준호",
                    posterUrl = "https://image.tmdb.org/t/p/w500/ib6v6qUXzez1x2qIOLN7C0yJNPQ.jpg",
                    year = 2013,
                ),
            )
    }
}