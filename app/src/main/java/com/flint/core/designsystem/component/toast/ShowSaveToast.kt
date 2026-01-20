package com.flint.core.designsystem.component.toast

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.snackbar.SaveToast
import com.flint.core.designsystem.theme.FlintTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun ShowSaveToast(
    navigateToSavedCollection: () -> Unit,
    paddingValues: PaddingValues,
    yOffset: Dp,
    hide: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(2.seconds)
        hide()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        SaveToast(
            navigateToSavedCollection = navigateToSavedCollection,
            modifier = Modifier
                .padding(paddingValues)
                .padding(bottom = yOffset),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShowSaveToastPreview() {
    FlintTheme {
        var show: Boolean by remember { mutableStateOf(true) }

        if (show) {
            ShowSaveToast(
                navigateToSavedCollection = {},
                paddingValues = PaddingValues.Zero,
                yOffset = 80.dp,
                hide = { show = false },
            )
        }
    }
}
