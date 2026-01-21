package com.flint.presentation.explore.uistate

import com.flint.domain.model.collection.CollectionsModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ExploreUiState(
    val collections: ImmutableList<CollectionsModel.Collection> = persistentListOf(),
    val currentCursor: Int = 1,
    val isLastPage: Boolean = false,
    val isLoadingMore: Boolean = false,
)
