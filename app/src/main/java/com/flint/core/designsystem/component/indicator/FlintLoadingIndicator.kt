package com.flint.core.designsystem.component.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintLoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = FlintTheme.colors.primary400
        )
    }
}
