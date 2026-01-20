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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun ShowToast(
    text: String,
    imageVector: ImageVector?,
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
        FlintToast(
            text = text,
            imageVector = imageVector,
            modifier = Modifier
                .padding(paddingValues)
                .padding(bottom = yOffset),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShowToastPreview() {
    FlintTheme {
        var show: Boolean by remember { mutableStateOf(true) }

        if (show) {
            ShowToast(
                text = "저장되었습니다",
                imageVector = ImageVector.vectorResource(R.drawable.ic_check),
                paddingValues = PaddingValues.Zero,
                yOffset = 80.dp,
                hide = { show = false },
            )
        }
    }
}

@Preview
@Composable
private fun ShowToastWithoutIconPreview() {
    FlintTheme {
        var show: Boolean by remember { mutableStateOf(true) }

        if (show) {
            ShowToast(
                text = "알림 메시지입니다",
                imageVector = null,
                paddingValues = PaddingValues.Zero,
                yOffset = 80.dp,
                hide = { show = false },
            )
        }
    }
}
