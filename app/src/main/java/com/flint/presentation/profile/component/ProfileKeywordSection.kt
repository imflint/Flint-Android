package com.flint.presentation.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.user.KeywordItemModel
import com.flint.domain.model.user.KeywordListModel
import com.flint.domain.type.KeywordType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun ProfileKeywordSection(
    nickname: String,
    keywordList: KeywordListModel,
    isMyProfile: Boolean,
    isRecalculatable: Boolean,
    showInfoModal: Boolean,
    onInfoClick: () -> Unit,
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${nickname}님의 취향키워드",
                        style = FlintTheme.typography.head3Sb18,
                        color = FlintTheme.colors.white,
                    )
                    if (isMyProfile) {
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(R.drawable.ic_info),
                            contentDescription = "취향키워드 정보",
                            tint = FlintTheme.colors.gray300,
                            modifier = Modifier
                                .size(20.dp)
                                .noRippleClickable { onInfoClick() },
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${nickname}님이 관심있어하는 키워드예요",
                    style = FlintTheme.typography.body2R14,
                    color = FlintTheme.colors.gray100,
                )
            }
            if (isMyProfile) {
                ProfileRefreshButton(
                    onRefreshClick = onRefreshClick,
                    isEnabled = isRecalculatable,
                )
            }
        }
        Spacer(Modifier.height(32.dp))
        Box {
            KeywordChipsGridLayout(
                keywordList = keywordList.keywords,
                modifier = Modifier.fillMaxWidth(),
            )
            if (isMyProfile && showInfoModal) {
                InfoModalTrigger(
                    text = "저장한 작품들에서 반복되는 키워드를 분석해 취향 키워드를 만들어요. 10개 이상 작품이 쌓이면 업데이트할 수 있어요.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-20).dp),
                )
            }
        }

        Spacer(Modifier.height(32.dp))
        KeywordGraphLayout(
            keywordList = keywordList.keywords,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ProfileRefreshButton(
    onRefreshClick: () -> Unit,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val iconTint = if (isEnabled) FlintTheme.colors.secondary400 else FlintTheme.colors.gray400
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier.noRippleClickable(
                onClick = { if (isEnabled) onRefreshClick() },
            ),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_refresh),
            contentDescription = null,
            tint = iconTint,
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
    keywordList: ImmutableList<KeywordItemModel>,
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
    keywordList: ImmutableList<KeywordItemModel>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        keywordList.take(3).forEach {
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
    keywordList: ImmutableList<KeywordItemModel>
): Pair<ImmutableList<KeywordItemModel>, Int> {
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
            keywordList = KeywordListModel.FakeList3,
            modifier = Modifier.fillMaxSize(),
            isMyProfile = true,
            isRecalculatable = true,
            showInfoModal = false,
            onInfoClick = {},
            onRefreshClick = {},
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun ProfileKeywordGraphLayoutPreview(
    @PreviewParameter(KeywordListPreviewParameterProvider::class)
    keywordList: KeywordListModel,
) {
    FlintTheme {
        KeywordChipsGridLayout(
            keywordList = keywordList.keywords,
            modifier = Modifier,
        )
    }
}

private class KeywordListPreviewParameterProvider :
    PreviewParameterProvider<KeywordListModel> {
    override val values: Sequence<KeywordListModel> = sequenceOf(
        KeywordListModel.FakeList1,
        KeywordListModel.FakeList2,
        KeywordListModel.FakeList3,
    )
}