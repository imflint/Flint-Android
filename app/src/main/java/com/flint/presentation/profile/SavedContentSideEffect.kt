package com.flint.presentation.profile

sealed interface SavedContentSideEffect {
    data class ToggleBookmarkSuccess(val isBookmarked: Boolean) : SavedContentSideEffect
}
