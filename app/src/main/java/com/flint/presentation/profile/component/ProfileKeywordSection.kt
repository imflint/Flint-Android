package com.flint.presentation.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kotlin.collections.sortedBy

private const val MAX = 3

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
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    Modifier
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
        Spacer(Modifier.height(32.dp))
        KeywordChipsLayout(
            keywordList = rotateKeywordByRank(keywordList),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun KeywordChipsLayout(
    keywordList: ImmutableList<UserKeywordResponseModel>,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
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
                        keyword = name,
                        keywordType = if (rank <= MAX) KeywordType.Large(preferenceType) else KeywordType.Small,
                        keywordImageUrl = imageUrl.orEmpty(),
                    )
                }
            }
        }
    }
}

@Composable
fun KeywordGraphLayout(
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

// TODO: 배치 로직 수정 필요
private fun rotateKeywordByRank(keywordList: ImmutableList<UserKeywordResponseModel>): ImmutableList<UserKeywordResponseModel> {
    if (keywordList.size < 2) return keywordList

    val sortedByRank = keywordList.sortedBy { it.rank }

    // rank 1, 2, 3 (상위 3개) → Large
    val topRanks = sortedByRank.take(3)
    // rank 4, 5, 6 (하위 3개) → Small
    val bottomRanks = sortedByRank.drop(3)

    // 번갈아 배치: 1, 4, 2, 5, 3, 6
    return topRanks
        .zip(bottomRanks)
        .flatMap { (top, bottom) -> listOf(top, bottom) }
        .toPersistentList()
}

@Preview(showBackground = false)
@Composable
private fun ProfileKeywordSectionPreview() {
    FlintTheme {
        ProfileKeywordSection(
            nickname = "안두콩",
            keywordList = UserKeywordResponseModel.FakeList1,
            modifier = Modifier.fillMaxSize(),
            onRefreshClick = {},
        )
    }
}
