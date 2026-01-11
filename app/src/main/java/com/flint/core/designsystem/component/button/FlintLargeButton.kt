package com.flint.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintLargeButton(
    text: String,
    state: FlintButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FlintBasicButton(
        text = text,
        state = state,
        onClick = onClick,
        contentPadding = PaddingValues(12.dp),
        modifier =
            modifier
                .padding(vertical = 0.dp)
                .defaultMinSize(minHeight = 48.dp),
    )
}

@Preview
@Composable
private fun FlintLargeButtonPreview() {
    FlintTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            FlintLargeButton(
                text = "시작하기",
                state = FlintButtonState.Able,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )

            FlintLargeButton(
                text = "시작하기",
                state = FlintButtonState.Disable,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )

            FlintLargeButton(
                text = "시작하기",
                state = FlintButtonState.ColorOutline,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )

            FlintLargeButton(
                text = "시작하기",
                state = FlintButtonState.Outline,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
