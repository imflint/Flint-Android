package com.flint.android.presentation.profile

sealed interface SavedContentSideEffect {
    data class ToggleBookmarkSuccess(val isBookmarked: Boolean) : SavedContentSideEffect
}
