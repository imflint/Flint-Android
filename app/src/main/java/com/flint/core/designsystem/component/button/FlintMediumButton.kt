package com.flint.core.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintMediumButton(
    text: String,
    state: FlintButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FlintBasicButton(
        text = text,
        state = state,
        onClick = onClick,
        verticalPadding = 2.dp,
        minHeight = 44.dp,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun FlintMediumButtonPreview() {
    FlintTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row {
                FlintMediumButton(
                    text = "시작하기",
                    state = FlintButtonState.Able,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )

                Spacer(Modifier.width(16.dp))

                FlintMediumButton(
                    text = "시작하기",
                    state = FlintButtonState.Outline,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )
            }

            Row {
                FlintMediumButton(
                    text = "시작하기",
                    state = FlintButtonState.Disable,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )

                Spacer(Modifier.width(16.dp))

                FlintMediumButton(
                    text = "시작하기",
                    state = FlintButtonState.ColorOutline,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
