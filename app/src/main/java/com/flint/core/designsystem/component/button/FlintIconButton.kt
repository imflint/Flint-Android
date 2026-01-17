package com.flint.core.designsystem.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintIconButton(
    text: String,
    @DrawableRes iconRes: Int,
    state: FlintButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FlintBasicButton(
        text = text,
        state = state,
        onClick = onClick,
        enabled = enabled,
        leadingIconRes = iconRes,
        contentPadding = PaddingValues(10.dp),
        modifier =
            modifier
                .padding(vertical = 2.dp)
                .defaultMinSize(minHeight = 44.dp),
    )
}

@Preview
@Composable
private fun FlintIconButtonPreview() {
    FlintTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row {
                FlintIconButton(
                    text = "공개",
                    iconRes = R.drawable.ic_share,
                    state = FlintButtonState.Disable,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )

                Spacer(Modifier.width(16.dp))

                FlintIconButton(
                    text = "공개",
                    iconRes = R.drawable.ic_share,
                    state = FlintButtonState.ColorOutline,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )
            }

            Row {
                FlintIconButton(
                    text = "공개",
                    iconRes = R.drawable.ic_share,
                    state = FlintButtonState.Outline,
                    onClick = {},
                    modifier = Modifier.weight(0.5f),
                )

                Spacer(Modifier.width(16.dp))

                FlintIconButton(
                    text = "비공개",
                    iconRes = R.drawable.ic_share,
                    state = FlintButtonState.Outline,
                    onClick = {},
                    modifier = Modifier.weight(0.5f),
                )
            }
        }
    }
}
