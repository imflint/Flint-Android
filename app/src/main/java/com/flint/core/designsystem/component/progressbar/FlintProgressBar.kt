package com.flint.core.designsystem.component.progressbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .height(4.dp)
                .clip(CircleShape)
                .background(FlintTheme.colors.gray600),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(FlintTheme.colors.secondary400),
        )
    }
}

@Preview
@Composable
private fun FlintProgressBarPreview(
    @PreviewParameter(FlintProgressBarPreviewParameterProvider::class) progress: Float,
) {
    FlintTheme {
        FlintProgressBar(progress, Modifier.fillMaxWidth())
    }
}

private class FlintProgressBarPreviewParameterProvider : PreviewParameterProvider<Float> {
    override val values: Sequence<Float> = sequenceOf(0f, 0.25f, 0.5f, 0.75f, 1f)
}
