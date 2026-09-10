package com.flint.android.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.android.core.common.extension.innerShadow
import com.flint.android.core.designsystem.component.image.NetworkImage
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.type.KeywordType
import com.flint.android.domain.type.PreferenceType

@Composable
fun ProfileKeywordChip(
    keyword: String,
    keywordType: KeywordType,
    keywordImageUrl: String = "",
) {
    when (keywordType) {
        is KeywordType.Small -> {
            ProfileSmallKeywordChip(
                keyword = keyword,
            )
        }

        is KeywordType.Large -> {
            ProfileLargeKeywordChip(
                keyword = keyword,
                keywordType = keywordType,
                imageUrl = keywordImageUrl,
            )
        }
    }
}

private fun Modifier.glassTagBackground(baseColor: Color): Modifier {
    val shape = RoundedCornerShape(percent = 50)
    return this
        .clip(shape)
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    baseColor.copy(alpha = 0.09f),
                    baseColor.copy(alpha = 0.44f),
                ),
            ),
        )
        .border(
            width = 1.dp,
            brush = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.White.copy(alpha = 0.30f),
                    0.77f to Color.White.copy(alpha = 0f),
                ),
            ),
            shape = shape,
        )
        // Effects > Inner shadow: #FFFFFF40(흰색 25%), offset (2, 4), blur 4, spread 0.
        // 입체감
        .innerShadow(
            shape = shape,
            color = Color.White.copy(alpha = 0.25f),
            blur = 4.dp,
            offsetX = 2.dp,
            offsetY = 4.dp,
        )
}

@Composable
private fun ProfileSmallKeywordChip(keyword: String) {
    Box(
        Modifier
            .glassTagBackground(FlintTheme.colors.gray500)
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp,
            ),
    ) {
        Text(
            text = keyword,
            style = FlintTheme.typography.body2R14,
            color = FlintTheme.colors.white,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun ProfileLargeKeywordChip(
    keyword: String,
    keywordType: KeywordType.Large,
    imageUrl: String,
) {
    Box(
        Modifier
            .glassTagBackground(keywordType.preferenceType.color)
            .padding(
                vertical = 12.dp,
                horizontal = 28.dp,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            NetworkImage(
                imageUrl = imageUrl,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = keyword,
                style = FlintTheme.typography.head2M20,
                color = FlintTheme.colors.white,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ProfileKeywordChipPreview() {
    FlintTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .background(FlintTheme.colors.background)
                .padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProfileKeywordChip(keyword = "슬픈", keywordType = KeywordType.Small)
                ProfileKeywordChip(keyword = "정체성", keywordType = KeywordType.Small)
            }

            PreferenceType.entries.forEach { preferenceType ->
                ProfileKeywordChip(
                    keyword = preferenceType.name,
                    keywordType = KeywordType.Large(preferenceType = preferenceType),
                    keywordImageUrl = "",
                )
            }
        }
    }
}
