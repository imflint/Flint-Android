package com.flint.presentation.collectiondetail

sealed interface CollectionDetailEvent {
    class ToggleCollectionBookmarkSuccess(val isBookmarked: Boolean) : CollectionDetailEvent

    object ToggleCollectionBookmarkFailure : CollectionDetailEvent
}