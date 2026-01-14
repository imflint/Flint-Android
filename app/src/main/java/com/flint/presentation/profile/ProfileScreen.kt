package com.flint.presentation.profile

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
fun ProfileRoute(
    paddingValues: PaddingValues,
    navigateToCollectionList: () -> Unit,
    navigateToSavedFilmList: () -> Unit,
    navigateToCollectionDetail: () -> Unit,
) {
    ProfileScreen(
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
private fun ProfileScreen(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .background(FlintTheme.colors.background)
                .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "ProfileScreen",
            color = FlintTheme.colors.white,
        )
    }
}
