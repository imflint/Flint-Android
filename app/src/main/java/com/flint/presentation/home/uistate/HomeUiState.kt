package com.flint.presentation.home.uistate

import com.flint.core.common.util.UiState
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.content.BookmarkedContentListModel

data class HomeUiState(
    val userName: String = "",
    val recommendedCollectionListLoadState: UiState<CollectionListModel> = UiState.Loading,
    val bookmarkedContentListLoadState: UiState<BookmarkedContentListModel> = UiState.Loading,
    val recentCollectionListLoadState: UiState<CollectionListModel> = UiState.Loading
) {
    val loadState: UiState<Unit>
        get() = when {
            recommendedCollectionListLoadState is UiState.Loading &&
            bookmarkedContentListLoadState is UiState.Loading &&
            recentCollectionListLoadState is UiState.Loading -> UiState.Loading

            recommendedCollectionListLoadState is UiState.Failure ||
            bookmarkedContentListLoadState is UiState.Failure ||
            recentCollectionListLoadState is UiState.Failure -> UiState.Failure

            recommendedCollectionListLoadState is UiState.Success &&
            bookmarkedContentListLoadState is UiState.Success &&
            recentCollectionListLoadState is UiState.Success -> UiState.Success(Unit)

            else -> UiState.Loading
        }
}
