package com.flint.presentation.explore.uistate

import com.flint.domain.model.collection.CollectionsModel
import kotlinx.collections.immutable.ImmutableList

data class ExploreUiState(
    val collections: ImmutableList<CollectionsModel.Collection>,
    val nextCursor: Long? = null,
    val isLastPage: Boolean = false,
    val isLoadingMore: Boolean = false,
)
