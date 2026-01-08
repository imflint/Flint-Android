package com.flint.presentation.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeRoute(
    paddingValues: PaddingValues,
) {
    HomeScreen(
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Text("home screen")
}