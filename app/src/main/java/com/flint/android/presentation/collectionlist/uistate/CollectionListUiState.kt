package com.flint.android.presentation.collectionlist.uistate

import com.flint.android.core.common.util.UiState
import com.flint.android.core.navigation.model.CollectionListRouteType
import com.flint.android.domain.model.collection.CollectionListModel

data class CollectionListUiState(
    val appbarTitle: String = "",
    /** 목록 표시 방식과, 상세 진입 시 분석용 source 를 정하는 데 함께 쓰인다. */
    val routeType: CollectionListRouteType = CollectionListRouteType.CREATED,
    val collectionList: UiState<CollectionListModel> = UiState.Loading
)