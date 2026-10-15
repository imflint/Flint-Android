package com.flint.android.presentation.explore.uistate

import com.flint.android.domain.model.exploration.ExplorationItemModel
import com.flint.android.domain.model.exploration.ExplorationState
import kotlinx.collections.immutable.ImmutableList

data class ExploreUiState(
    val items: ImmutableList<ExplorationItemModel>,
    val state: ExplorationState,
    val initialPage: Int = 0,
) {
    val isEnd: Boolean
        get() = state == ExplorationState.END

    val isEmpty: Boolean
        get() = state == ExplorationState.EMPTY
}
