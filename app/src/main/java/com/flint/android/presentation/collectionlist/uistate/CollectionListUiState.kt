package com.flint.android.presentation.collectionlist.uistate

import com.flint.android.core.common.util.UiState
import com.flint.android.domain.model.collection.CollectionListModel

data class CollectionListUiState(
    val appbarTitle: String = "",
    val collectionList: UiState<CollectionListModel> = UiState.Loading
)