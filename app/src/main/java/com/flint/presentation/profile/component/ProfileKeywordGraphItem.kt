package com.flint.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.type.PreferenceType

@Composable
fun ProfileKeywordGraphItem(
    keyword: String,
    preferenceType: PreferenceType,
    percentage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(preferenceType.color),
            )
            Text(
                text = keyword,
                style = FlintTheme.typography.body1M16,
                color = FlintTheme.colors.white,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ProfileKeywordProgressBar(
                preferenceType = preferenceType,
                percent = percentage.toFloat() / 100,
                modifier = Modifier.width(160.dp),
            )
            Text(
                text = "$percentage%",
                style = FlintTheme.typography.body2R14,
                color = FlintTheme.colors.gray100,
                textAlign = TextAlign.End,
                modifier = Modifier.widthIn(min = 32.dp),
            )
        }
    }
}

@Composable
private fun ProfileKeywordProgressBar(
    preferenceType: PreferenceType,
    percent: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .height(12.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(FlintTheme.colors.gray500),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(percent)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(preferenceType.color),
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun ProfileKeywordGraphPreview() {
    FlintTheme {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().padding(12.dp),
        ) {
            itemsIndexed(
                listOf(
                    Triple("환경", PreferenceType.GREEN, 75),
                    Triple("동물", PreferenceType.ORANGE, 50),
                    Triple("패션", PreferenceType.YELLOW, 30),
                ),
            ) { _, item ->
                ProfileKeywordGraphItem(
                    keyword = item.first,
                    preferenceType = item.second,
                    percentage = item.third,
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                )
            }
        }
    }
}
