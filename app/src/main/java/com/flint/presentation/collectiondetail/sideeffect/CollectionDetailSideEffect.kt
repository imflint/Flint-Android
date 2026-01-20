package com.flint.presentation.collectiondetail.sideeffect

sealed interface CollectionDetailSideEffect {
    class ToggleCollectionBookmarkSuccess(val isBookmarked: Boolean) : CollectionDetailSideEffect

    object ToggleCollectionBookmarkFailure : CollectionDetailSideEffect
}
