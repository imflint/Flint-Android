package com.flint.android.core.navigation.model

enum class CollectionListRouteType(
    val title: String
) {
    CREATED(title = "전체 컬렉션"),
    SAVED(title = "저장 컬렉션"),
    FAMOUS(title = "인기 컬렉션")
}