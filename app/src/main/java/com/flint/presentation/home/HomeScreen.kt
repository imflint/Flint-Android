package com.flint.presentation.home

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
fun HomeRoute(paddingValues: PaddingValues) {
    HomeScreen(
        modifier = Modifier.padding(paddingValues)
    )
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier =
        modifier
            .background(FlintTheme.colors.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "HomeScreen",
            color = FlintTheme.colors.white
        )
    }
}
