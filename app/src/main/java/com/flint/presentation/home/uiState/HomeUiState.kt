package com.flint.presentation.home.uiState

import com.flint.core.common.util.UiState
import com.flint.domain.model.collection.CollectionModel
import com.flint.domain.model.content.ContentModel

data class HomeUiState(
    val recommendedCollectionListLoadState: UiState<List<CollectionModel>> = UiState.Loading,
    val bookmarkedContentListLoadState: UiState<List<ContentModel>> = UiState.Loading,
) {
    val loadState: UiState<Unit>
        get() = when {
//            recommendedCollectionListLoadState is UiState.Loading &&
//            bookmarkedContentListLoadState is UiState.Loading &&
//            favoriteListLoadState is UiState.Loading -> UiState.Loading
//
//            recommendedCollectionListLoadState is UiState.Failure ||
//            bookmarkedContentListLoadState is UiState.Failure ||
//            favoriteListLoadState is UiState.Failure -> UiState.Failure
//
//            recommendedCollectionListLoadState is UiState.Success &&
//            bookmarkedContentListLoadState is UiState.Success &&
//            favoriteListLoadState is UiState.Success -> UiState.Success(Unit)

            else -> UiState.Loading
        }
}
