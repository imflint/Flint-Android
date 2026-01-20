package com.flint.presentation.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.user.UserKeywordResponseModel
import com.flint.domain.type.KeywordType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun ProfileKeywordSection(
    nickname: String,
    keywordList: ImmutableList<UserKeywordResponseModel>,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column {
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
            }
        }
        Spacer(Modifier.height(32.dp))
        KeywordChipsGridLayout(
            keywordList = keywordList,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ProfileRefreshButton(
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .noRippleClickable(
                    onClick = onRefreshClick,
                ),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_refresh),
            contentDescription = null,
            tint = FlintTheme.colors.secondary400,
        )
        Text(
            text = "업데이트",
            style = FlintTheme.typography.micro1M10,
            color = FlintTheme.colors.gray100,
        )
    }
}

@Composable
private fun KeywordChipsGridLayout(
    keywordList: ImmutableList<UserKeywordResponseModel>,
    modifier: Modifier = Modifier,
) {
    val (arrangedKeywords, itemsPerRow) = remember(keywordList) {
        arrangeKeywordsByRank(keywordList)
    }

    val horizontalSpacing = if (itemsPerRow == 3) 8.dp else 20.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        arrangedKeywords.chunked(itemsPerRow).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp),
            ) {
                rowItems.forEach { keyword ->
                    val keywordType = if (keyword.rank <= 3) {
                        KeywordType.Large(keyword.preferenceType)
                    } else {
                        KeywordType.Small
                    }

                    ProfileKeywordChip(
                        keyword = keyword.name,
                        keywordType = keywordType,
                        keywordImageUrl = keyword.imageUrl,
                    )
                }
            }
        }
    }
}

@Composable
private fun KeywordGraphLayout(
    keywordList: ImmutableList<UserKeywordResponseModel>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        keywordList.forEach {
            with(it) {
                ProfileKeywordGraphItem(
                    keyword = name,
                    preferenceType = preferenceType,
                    percentage = percentage.toInt(),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

private fun arrangeKeywordsByRank(
    keywordList: ImmutableList<UserKeywordResponseModel>
): Pair<ImmutableList<UserKeywordResponseModel>, Int> {
    if (keywordList.size < 6) return keywordList to 3

    val sortedByRank = keywordList.sortedBy { it.rank }

    val checkTargets = listOf(
        sortedByRank[0], // rank 1
        sortedByRank[1], // rank 2
        sortedByRank[3], // rank 4
    )

    val shouldUseThreeRows = checkTargets.any { it.name.length >= 3 }

    return if (shouldUseThreeRows) {
        // 3줄 배치: [1, 4], [5, 2], [3, 6]
        val threeRowOrder = listOf(0, 3, 4, 1, 2, 5)
        threeRowOrder.map { sortedByRank[it] }.toPersistentList() to 2
    } else {
        // 2줄 배치: [1, 4, 2], [5, 3, 6]
        val twoRowOrder = listOf(0, 3, 1, 4, 2, 5)
        twoRowOrder.map { sortedByRank[it] }.toPersistentList() to 3
    }
}

@Preview(showBackground = false)
@Composable
private fun ProfileKeywordSectionPreview() {
    FlintTheme {
        ProfileKeywordSection(
            nickname = "안두콩",
            keywordList = UserKeywordResponseModel.FakeList3,
            modifier = Modifier.fillMaxSize(),
            onRefreshClick = {},
        )
    }
}
