package com.flint.presentation.explore

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ExploreRoute(
    paddingValues: PaddingValues,
) {
    ExploreScreen(
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
private fun ExploreScreen(
    modifier: Modifier = Modifier
) {
    Text("explore screen")
}