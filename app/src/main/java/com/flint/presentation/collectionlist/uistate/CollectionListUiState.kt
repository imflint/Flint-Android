package com.flint.presentation.collectionlist.uistate

import com.flint.core.common.util.UiState
import com.flint.domain.model.collection.CollectionListModel

data class CollectionListUiState(
    val appbarTitle: String = "",
    val collectionList: UiState<CollectionListModel> = UiState.Loading
)