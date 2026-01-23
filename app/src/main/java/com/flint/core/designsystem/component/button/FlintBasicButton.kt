package com.flint.core.designsystem.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintBasicButton(
    text: String,
    state: FlintButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues,
    @DrawableRes leadingIconRes: Int? = null,
) {
    val background: Brush = state.background
    val contentColor: Color = state.contentColor
    val border: BorderStroke? = state.border
    val shape = RoundedCornerShape(8.dp)

    Row(
        modifier =
            modifier
                .run {
                    if (border != null) {
                        border(border = border, shape = shape)
                    } else {
                        this
                    }
                }
                .clip(shape)
                .background(background)
                .clickable(enabled = enabled, onClick = onClick)
                .padding(contentPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIconRes != null) {
            Icon(
                painter = painterResource(leadingIconRes),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(24.dp),
                tint = contentColor,
            )
        }

        Text(
            text = text,
            color = contentColor,
            style = if (state == FlintButtonState.Able) FlintTheme.typography.body1Sb16 else FlintTheme.typography.body1M16,
        )
    }
}

@Preview
@Composable
private fun FlintBasicButtonPreview() {
    FlintTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            FlintBasicButton(
                text = "시작하기",
                state = FlintButtonState.Able,
                onClick = {},
                contentPadding = PaddingValues(12.dp),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 0.dp)
                        .defaultMinSize(minHeight = 48.dp),
            )

            FlintBasicButton(
                text = "시작하기",
                state = FlintButtonState.Disable,
                onClick = {},
                contentPadding = PaddingValues(12.dp),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 0.dp)
                        .defaultMinSize(minHeight = 48.dp),
            )

            FlintBasicButton(
                text = "시작하기",
                state = FlintButtonState.ColorOutline,
                onClick = {},
                contentPadding = PaddingValues(12.dp),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 0.dp)
                        .defaultMinSize(minHeight = 48.dp),
            )

            FlintBasicButton(
                text = "시작하기",
                state = FlintButtonState.Outline,
                onClick = {},
                contentPadding = PaddingValues(12.dp),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 0.dp)
                        .defaultMinSize(minHeight = 48.dp),
            )

            Row {
                FlintBasicButton(
                    text = "시작하기",
                    state = FlintButtonState.Able,
                    onClick = {},
                    contentPadding = PaddingValues(10.dp),
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(vertical = 2.dp)
                            .defaultMinSize(minHeight = 44.dp),
                )

                Spacer(Modifier.width(16.dp))

                FlintBasicButton(
                    text = "시작하기",
                    state = FlintButtonState.Outline,
                    onClick = {},
                    contentPadding = PaddingValues(10.dp),
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(vertical = 2.dp)
                            .defaultMinSize(minHeight = 44.dp),
                )
            }

            Row {
                FlintBasicButton(
                    text = "시작하기",
                    state = FlintButtonState.Disable,
                    onClick = {},
                    contentPadding = PaddingValues(10.dp),
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(vertical = 2.dp)
                            .defaultMinSize(minHeight = 44.dp),
                )

                Spacer(Modifier.width(16.dp))

                FlintBasicButton(
                    text = "시작하기",
                    state = FlintButtonState.ColorOutline,
                    onClick = {},
                    contentPadding = PaddingValues(10.dp),
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(vertical = 2.dp)
                            .defaultMinSize(minHeight = 44.dp),
                )
            }

            Row {
                FlintBasicButton(
                    text = "공개",
                    state = FlintButtonState.Disable,
                    onClick = {},
                    contentPadding = PaddingValues(10.dp),
                    leadingIconRes = R.drawable.ic_share,
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(vertical = 2.dp)
                            .defaultMinSize(minHeight = 44.dp),
                )

                Spacer(Modifier.width(16.dp))

                FlintBasicButton(
                    text = "공개",
                    state = FlintButtonState.ColorOutline,
                    onClick = {},
                    contentPadding = PaddingValues(10.dp),
                    leadingIconRes = R.drawable.ic_share,
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(vertical = 2.dp)
                            .defaultMinSize(minHeight = 44.dp),
                )
            }

            Row {
                FlintBasicButton(
                    text = "공개",
                    state = FlintButtonState.Outline,
                    onClick = {},
                    contentPadding = PaddingValues(10.dp),
                    leadingIconRes = R.drawable.ic_share,
                    modifier =
                        Modifier
                            .weight(0.5f)
                            .padding(vertical = 2.dp)
                            .defaultMinSize(minHeight = 44.dp),
                )

                Spacer(Modifier.width(16.dp))

                Spacer(Modifier.weight(0.5f))
            }
        }
    }
}
