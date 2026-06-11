package com.flint.presentation.home.uistate

import com.flint.core.common.util.UiState
import com.flint.domain.model.collection.CollectionListModel

data class HomeUiState(
    val userName: String = "",
    val recommendedCollectionListLoadState: UiState<CollectionListModel> = UiState.Loading,
    val bookmarkedCollectionListLoadState: UiState<CollectionListModel> = UiState.Loading,
    val popularCollectionListLoadState: UiState<CollectionListModel> = UiState.Loading
) {
    val loadState: UiState<Unit>
        get() = when {
            recommendedCollectionListLoadState is UiState.Loading &&
            bookmarkedCollectionListLoadState is UiState.Loading &&
            popularCollectionListLoadState is UiState.Loading -> UiState.Loading

            recommendedCollectionListLoadState is UiState.Failure ||
            bookmarkedCollectionListLoadState is UiState.Failure ||
            popularCollectionListLoadState is UiState.Failure -> UiState.Failure

            recommendedCollectionListLoadState is UiState.Success &&
            bookmarkedCollectionListLoadState is UiState.Success &&
            popularCollectionListLoadState is UiState.Success -> UiState.Success(Unit)

            else -> UiState.Loading
        }
}
