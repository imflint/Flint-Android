package com.flint.domain.type

/** 앱 내에 저장될 enum 등의 타입 지정*/
enum class SampleType(val title: String) {
    DRAMA("드라마"),
    VARIETY("예능"),
    MOVIE("영화"),
    SPORTS("스포츠"),
    ANIMATION("애니메이션"),
    NEWS("뉴스");

    companion object {
        // id를 통해 TabGenreType 얻기
        fun getGenreById(index: Int) = entries.find { // 반환 타입: TabGenreType
            it.ordinal == index
        } ?: DRAMA

        // 각 상수의 title을 목록으로 받아오기
        fun getGenreTitles() = entries.map { // 반환 타입: List<String>
            it.title
        }
    }
}