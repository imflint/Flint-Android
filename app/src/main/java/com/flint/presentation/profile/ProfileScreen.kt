package com.flint.presentation.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProfileRoute(
    paddingValues: PaddingValues,
) {
    ProfileScreen(
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
private fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    Text("profile screen")
}