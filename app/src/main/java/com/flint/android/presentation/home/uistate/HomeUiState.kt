package com.flint.android.presentation.home.uistate

import com.flint.android.core.common.util.UiState
import com.flint.android.domain.model.collection.CollectionListModel
import com.flint.android.domain.model.content.BookmarkedContentListModel

data class HomeUiState(
    val userName: String = "",
    val recommendedCollectionListLoadState: UiState<CollectionListModel> = UiState.Loading,
    val bookmarkedContentListLoadState: UiState<BookmarkedContentListModel> = UiState.Loading,
    val popularCollectionListLoadState: UiState<CollectionListModel> = UiState.Loading
) {
    val loadState: UiState<Unit>
        get() = when {
            recommendedCollectionListLoadState is UiState.Loading &&
            bookmarkedContentListLoadState is UiState.Loading &&
            popularCollectionListLoadState is UiState.Loading -> UiState.Loading

            recommendedCollectionListLoadState is UiState.Failure ||
            bookmarkedContentListLoadState is UiState.Failure ||
            popularCollectionListLoadState is UiState.Failure -> UiState.Failure

            recommendedCollectionListLoadState is UiState.Success &&
            bookmarkedContentListLoadState is UiState.Success &&
            popularCollectionListLoadState is UiState.Success -> UiState.Success(Unit)

            else -> UiState.Loading
        }
}
