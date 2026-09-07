package com.flint.android.presentation.collectionlist.uistate

import com.flint.android.core.common.util.UiState
import com.flint.android.core.navigation.model.CollectionListRouteType
import com.flint.android.domain.model.collection.CollectionListModel

data class CollectionListUiState(
    val appbarTitle: String = "",
    val routeType: CollectionListRouteType = CollectionListRouteType.CREATED,
    val collectionList: UiState<CollectionListModel> = UiState.Loading
)