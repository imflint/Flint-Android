package com.flint.android.core.navigation.model

import com.flint.android.core.analytics.CollectionSource

enum class CollectionListRouteType(
    val title: String
) {
    CREATED(title = "전체 컬렉션"),
    SAVED(title = "저장 컬렉션"),
    FAMOUS(title = "인기 컬렉션")
}

/**
 * 목록을 거쳐 상세로 들어가도 원래 어느 지면에서 출발했는지를 유지한다.
 * 정의서에 정의된 5개 값 안에서 표현한다.
 */
fun CollectionListRouteType.toCollectionSource(): CollectionSource =
    when (this) {
        CollectionListRouteType.CREATED -> CollectionSource.MY_CREATED
        CollectionListRouteType.SAVED -> CollectionSource.MY_SAVED
        CollectionListRouteType.FAMOUS -> CollectionSource.HOME_POPULAR
    }
