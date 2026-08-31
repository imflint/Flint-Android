package com.flint.android.presentation.collectionlist.uistate

import com.flint.android.core.common.util.UiState
import com.flint.android.core.navigation.model.CollectionListRouteType
import com.flint.android.domain.model.collection.CollectionListModel

data class CollectionListUiState(
    val appbarTitle: String = "",
    /** 상세 진입 시 분석용 source 를 정하기 위해 보관한다. */
    val routeType: CollectionListRouteType = CollectionListRouteType.FAMOUS,
    val collectionList: UiState<CollectionListModel> = UiState.Loading
)