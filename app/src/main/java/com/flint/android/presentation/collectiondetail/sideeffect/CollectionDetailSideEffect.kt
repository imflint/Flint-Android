package com.flint.android.presentation.collectiondetail.sideeffect

sealed interface CollectionDetailSideEffect {
    class ToggleCollectionBookmarkSuccess(val isBookmarked: Boolean) : CollectionDetailSideEffect

    object ToggleCollectionBookmarkFailure : CollectionDetailSideEffect

    class ToggleContentBookmarkSuccess(val isBookmarked: Boolean) : CollectionDetailSideEffect

    object ToggleContentBookmarkMinLimitExceeded : CollectionDetailSideEffect

    object DeleteCollectionSuccess : CollectionDetailSideEffect

    object DeleteCollectionFailure : CollectionDetailSideEffect
}
