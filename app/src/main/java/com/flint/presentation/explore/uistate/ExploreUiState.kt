package com.flint.presentation.explore.uistate

import com.flint.domain.model.exploration.ExplorationItemModel
import com.flint.domain.model.exploration.ExplorationState
import kotlinx.collections.immutable.ImmutableList

data class ExploreUiState(
    val items: ImmutableList<ExplorationItemModel>,
    val state: ExplorationState,
    val hasNext: Boolean = false,
    val isLoadingNext: Boolean = false,
    val initialPage: Int = 0,
) {
    val isEnd: Boolean
        get() = state == ExplorationState.END

    val isEmpty: Boolean
        get() = state == ExplorationState.EMPTY

    val canAdvance: Boolean
        get() = !isLoadingNext && state == ExplorationState.IN_PROGRESS
}
