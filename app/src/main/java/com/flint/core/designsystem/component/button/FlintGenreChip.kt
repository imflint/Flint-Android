package com.flint.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintGenreChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 9.dp),
) {
    val shape = CircleShape

    val backgroundColor =
        if (isSelected) FlintTheme.colors.primary400 else FlintTheme.colors.gray800

    val border: BorderStroke? =
        if (isSelected) null else BorderStroke(1.dp, FlintTheme.colors.gray300)

    val contentColor = FlintTheme.colors.white

    Row(
        modifier =
            modifier
                .clip(shape)
                .run {
                    if (border != null)
                        border(border = border, shape = shape)
                    else
                        this
                }
                .background(color = backgroundColor, shape = shape)
                .noRippleClickable(enabled = enabled, onClick = onClick)
                .padding(contentPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            color = contentColor,
            style = FlintTheme.typography.body2M14,
        )
    }
}

@Preview
@Composable
private fun FlintGenreChipPreview() {
    FlintTheme {
        Box(
            modifier =
                Modifier
                    .background(FlintTheme.colors.background)
                    .fillMaxWidth()
                    .padding(20.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FlintGenreChip(
                    text = "액션",
                    isSelected = false,
                    onClick = {},
                )

                FlintGenreChip(
                    text = "액션",
                    isSelected = true,
                    onClick = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun FlintGenreChipInteractivePreview() {
    FlintTheme {
        val genres = listOf("액션", "코미디", "로맨스", "스릴러", "드라마", "SF", "공포", "다큐")
        var selected by remember { mutableStateOf(setOf("액션", "로맨스")) }

        Box(
            modifier =
                Modifier
                    .background(FlintTheme.colors.background)
                    .fillMaxWidth()
                    .padding(20.dp),
        ) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                genres.forEach { genre ->
                    FlintGenreChip(
                        text = genre,
                        isSelected = genre in selected,
                        onClick = {
                            selected =
                                if (genre in selected) {
                                    selected - genre
                                } else {
                                    selected + genre
                                }
                        },
                    )
                }
            }
        }
    }
}