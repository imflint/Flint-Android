package com.flint.domain.type

enum class CollectionListRouteType(
    val title: String
) {
    CREATED(title = "전체 컬렉션"),
    SAVED(title = "저장 컬렉션"),
    RECENT(title = "눈여겨보고 있는 컬렉션"),
}