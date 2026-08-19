package com.flint.android.domain.model.exploration

import kotlinx.collections.immutable.ImmutableList

enum class ExplorationState {
    IN_PROGRESS,
    END,
    EMPTY,
    UNKNOWN,
}

data class ExplorationSessionModel(
    val items: ImmutableList<ExplorationItemModel>,
    val state: ExplorationState,
    val hasNext: Boolean,
) {
    val isEnd: Boolean
        get() = state == ExplorationState.END

    val isEmpty: Boolean
        get() = state == ExplorationState.EMPTY
}

data class ExplorationItemModel(
    val contentId: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val year: Int,
    val collectionId: String,
)
