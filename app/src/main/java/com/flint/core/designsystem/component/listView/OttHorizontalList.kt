package com.flint.core.designsystem.component.listView

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.flint.core.common.extension.dropShadow
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.core.common.type.OttType

private const val MAX_OTT_SHOWING_COUNT = 2

@Composable
fun OttHorizontalList(
    ottList: List<OttType>,
    modifier: Modifier = Modifier,
) {
    if (ottList.isEmpty()) return

    val firstOttIcon = ottList[0].iconRes

    val secondOttIcon =
        if (ottList.size > 1) {
            ottList[1].iconRes
        } else {
            null
        }

    val etcOttText =
        if (ottList.size > MAX_OTT_SHOWING_COUNT) {
            "+${ottList.size - MAX_OTT_SHOWING_COUNT}"
        } else {
            "0"
        }

    Row(
        modifier = modifier,
    ) {
        Image(
            painter = rememberAsyncImagePainter(firstOttIcon),
            contentDescription = null,
            modifier =
                Modifier
                    .size(28.dp)
                    .dropShadow(
                        shape = CircleShape,
                        color = Color(0xFF000000).copy(alpha = 0.35f),
                        offsetX = (-4).dp,
                        offsetY = 0.dp,
                        blur = 6.dp,
                        spread = 0.dp,
                    ),
        )

        if (secondOttIcon != null) {
            Image(
                painter = rememberAsyncImagePainter(secondOttIcon),
                contentDescription = null,
                modifier =
                    Modifier
                        .size(28.dp)
                        .offset(x = (-8).dp)
                        .dropShadow(
                            shape = CircleShape,
                            color = Color(0xFF000000).copy(alpha = 0.35f),
                            offsetX = (-4).dp,
                            offsetY = 0.dp,
                            blur = 6.dp,
                            spread = 0.dp,
                        ),
            )
        }

        if (etcOttText != "0") {
            Box(
                modifier =
                    Modifier
                        .size(28.dp)
                        .offset(x = (-16).dp)
                        .dropShadow(
                            shape = CircleShape,
                            color = Color(0xFF000000).copy(alpha = 0.35f),
                            offsetX = (-4).dp,
                            offsetY = 0.dp,
                            blur = 6.dp,
                            spread = 0.dp,
                        ).clip(CircleShape)
                        .background(FlintTheme.colors.gray500),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = etcOttText,
                    color = FlintTheme.colors.white,
                    style = FlintTheme.typography.body2M14,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewOttHorizontalList() {
    FlintTheme {
        val ottList =
            listOf(
                OttType.Netflix,
                OttType.Disney,
                OttType.Tving,
                OttType.Coupang,
                OttType.Wave,
            )

        OttHorizontalList(
            ottList = ottList,
        )
    }
}
