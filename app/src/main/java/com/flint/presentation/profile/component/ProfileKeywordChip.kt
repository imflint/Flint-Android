package com.flint.presentation.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.draw9Patch
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.type.KeywordType
import com.flint.domain.type.PreferenceType

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

@Composable
private fun ProfileSmallKeywordChip(keyword: String) {
    Box(
        Modifier
            .widthIn(min = 64.dp)
            .draw9Patch(LocalContext.current, R.drawable.bg_tag_gray)
            .padding(
                vertical = 8.dp,
                horizontal = 7.dp,
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
            .draw9Patch(LocalContext.current, keywordType.preferenceType.backgroundRes)
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

@Preview(showBackground = true)
@Composable
private fun ProfileKeywordChipPreview() {
    FlintTheme {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileKeywordChip(
                keyword = "슬픈",
                keywordType = KeywordType.Small,
            )
            ProfileKeywordChip(
                keyword = "영화",
                keywordType =
                    KeywordType.Large(
                        preferenceType = PreferenceType.Blue,
                    ),
                keywordImageUrl = "",
            )
        }
    }
}
