package com.flint.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme

enum class FlintButtonType(
    val verticalPadding: Dp,
    val minHeight: Dp,
) {
    Large(verticalPadding = 0.dp, minHeight = 48.dp),
    Medium(verticalPadding = 2.dp, minHeight = 44.dp),
}

enum class FlintButtonState(
    val enabled: Boolean,
) {
    Able(enabled = true),
    Disable(enabled = false),
    Outline(enabled = true),
    ColorOutline(enabled = true),
    ;

    val background: Brush
        @Composable
        @ReadOnlyComposable
        get() =
            when (this) {
                Able -> FlintTheme.colors.gradient400
                Disable -> SolidColor(FlintTheme.colors.gray700)
                Outline, ColorOutline -> SolidColor(FlintTheme.colors.gray800)
            }

    val contentColor: Color
        @Composable
        @ReadOnlyComposable
        get() =
            when (this) {
                Able, Outline, ColorOutline -> FlintTheme.colors.white
                Disable -> FlintTheme.colors.gray400
            }

    val border: BorderStroke?
        @Composable
        @ReadOnlyComposable
        get() =
            when (this) {
                Outline -> BorderStroke(2.dp, FlintTheme.colors.gray500)
                ColorOutline -> BorderStroke(2.dp, FlintTheme.colors.primary400)
                else -> null
            }
}

@Composable
fun FlintButton(
    text: String,
    type: FlintButtonType,
    state: FlintButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: Painter? = null,
) {
    val verticalPadding: Dp = type.verticalPadding
    val minHeight: Dp = type.minHeight
    val enabled: Boolean = state.enabled
    val background: Brush = state.background
    val contentColor: Color = state.contentColor
    val border: BorderStroke? = state.border
    val shape = RoundedCornerShape(8.dp)

    Row(
        modifier =
            modifier
                .padding(vertical = verticalPadding)
                .run {
                    if (border != null) {
                        border(border = border, shape = shape)
                    } else {
                        this
                    }
                }.clip(shape)
                .background(background)
                .clickable(enabled = enabled, onClick = onClick)
                .defaultMinSize(minHeight = minHeight),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            Icon(
                painter = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = contentColor,
            )

            if (text.isNotEmpty()) Spacer(Modifier.width(8.dp))
        }

        Text(
            text = text,
            color = contentColor,
            style = if (enabled) FlintTheme.typography.body1Sb16 else FlintTheme.typography.body1M16,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FlintButtonPreview() {
    FlintTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            FlintButton(
                text = "시작하기",
                type = FlintButtonType.Large,
                state = FlintButtonState.Able,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )

            FlintButton(
                text = "시작하기",
                type = FlintButtonType.Large,
                state = FlintButtonState.Disable,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )

            FlintButton(
                text = "시작하기",
                type = FlintButtonType.Large,
                state = FlintButtonState.ColorOutline,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )

            FlintButton(
                text = "시작하기",
                type = FlintButtonType.Large,
                state = FlintButtonState.Outline,
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )

            Row {
                FlintButton(
                    text = "시작하기",
                    type = FlintButtonType.Medium,
                    state = FlintButtonState.Able,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )

                Spacer(Modifier.width(16.dp))

                FlintButton(
                    text = "시작하기",
                    type = FlintButtonType.Medium,
                    state = FlintButtonState.Outline,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )
            }

            Row {
                FlintButton(
                    text = "시작하기",
                    type = FlintButtonType.Medium,
                    state = FlintButtonState.Disable,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )

                Spacer(Modifier.width(16.dp))

                FlintButton(
                    text = "시작하기",
                    type = FlintButtonType.Medium,
                    state = FlintButtonState.ColorOutline,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                )
            }

            Row {
                FlintButton(
                    text = "공개",
                    type = FlintButtonType.Medium,
                    state = FlintButtonState.Disable,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    leadingIcon = painterResource(R.drawable.ic_share),
                )

                Spacer(Modifier.width(16.dp))

                FlintButton(
                    text = "공개",
                    type = FlintButtonType.Medium,
                    state = FlintButtonState.ColorOutline,
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    leadingIcon = painterResource(R.drawable.ic_share),
                )
            }

            Row {
                FlintButton(
                    text = "공개",
                    type = FlintButtonType.Medium,
                    state = FlintButtonState.Outline,
                    onClick = {},
                    modifier = Modifier.weight(0.5f),
                    leadingIcon = painterResource(R.drawable.ic_share),
                )

                Spacer(Modifier.width(16.dp))

                Spacer(Modifier.weight(0.5f))
            }
        }
    }
}
