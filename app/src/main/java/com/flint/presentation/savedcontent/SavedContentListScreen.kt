package com.flint.presentation.savedcontent

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.flint.presentation.profile.SavedContentRoute

@Composable
fun SavedContentListRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    SavedContentRoute(
        paddingValues = paddingValues,
        navigateUp = navigateUp,
    )
}
