package com.flint.presentation.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun ExploreRoute(
    paddingValues: PaddingValues,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
    navigateToCollectionCreate: () -> Unit,
) {
    ExploreScreen(
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
private fun ExploreScreen(modifier: Modifier) {
    Box(
        modifier =
            modifier
                .background(FlintTheme.colors.background)
                .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            "ExploreScreen",
            color = FlintTheme.colors.white,
        )
    }
}
