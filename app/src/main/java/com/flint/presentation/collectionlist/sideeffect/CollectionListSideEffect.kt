package com.flint.presentation.collectionlist.sideeffect

sealed interface CollectionListSideEffect {
    data class ToggleCollectionBookmarkSuccess(val isBookmarked: Boolean) : CollectionListSideEffect

    data object ToggleCollectionBookmarkFailure : CollectionListSideEffect
}
