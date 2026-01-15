package com.flint.presentation.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.PreferenceKeywordModel
import com.flint.domain.model.PreferenceKeywordModel.Companion.rotateKeywordByRank
import com.flint.domain.type.KeywordType
import kotlinx.collections.immutable.ImmutableList

private const val MAX = 3

@Composable
fun ProfileKeywordSection(
    nickname: String,
    keywordList: ImmutableList<PreferenceKeywordModel>,
    modifier: Modifier = Modifier,
) {
    val rotatedKeywordList =
        remember(keywordList) {
            rotateKeywordByRank(keywordList)
        }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = "${nickname}님의 취향키워드",
            style = FlintTheme.typography.head3Sb18,
            color = FlintTheme.colors.white,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "${nickname}님이 관심있어하는 키워드에요",
            style = FlintTheme.typography.body2R14,
            color = FlintTheme.colors.gray100,
        )
        Spacer(Modifier.height(32.dp))
        KeywordChipsLayout(
            keywordList = rotatedKeywordList,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(32.dp))
        KeywordGraphLayout(
            keywordList = keywordList.subList(0, MAX),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun KeywordChipsLayout(
    keywordList: ImmutableList<PreferenceKeywordModel>,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            itemVerticalAlignment = Alignment.CenterVertically,
            maxItemsInEachRow = 3,
            modifier =
                Modifier
                    .align(Alignment.Center),
        ) {
            keywordList.forEach {
                with(it) {
                    ProfileKeywordChip(
                        keyword = title,
                        keywordType = if (rank <= MAX) KeywordType.Large(level) else KeywordType.Small,
                        keywordImageUrl = imageUrl.orEmpty(),
                    )
                }
            }
        }
    }
}

@Composable
fun KeywordGraphLayout(
    keywordList: ImmutableList<PreferenceKeywordModel>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        keywordList.forEach {
            with(it) {
                ProfileKeywordGraphItem(
                    keyword = title,
                    preferenceType = level,
                    percentage = percentage,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun ProfileKeywordSectionPreview() {
    FlintTheme {
        ProfileKeywordSection(
            nickname = "안두콩",
            keywordList = PreferenceKeywordModel.FakeList1,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
