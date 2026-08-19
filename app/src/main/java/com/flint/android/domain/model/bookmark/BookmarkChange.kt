package com.flint.android.domain.model.bookmark

sealed class BookmarkChange {
    abstract val id: String
    abstract val isBookmarked: Boolean

    data class Content(
        override val id: String,
        override val isBookmarked: Boolean,
    ) : BookmarkChange()

    data class Collection(
        override val id: String,
        override val isBookmarked: Boolean,
    ) : BookmarkChange()
}
