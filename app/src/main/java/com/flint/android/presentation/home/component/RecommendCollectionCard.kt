package com.flint.android.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flint.android.core.designsystem.interaction.flintCardClickable
import com.flint.android.core.designsystem.component.image.NetworkImage
import com.flint.android.core.designsystem.component.image.ProfileImage
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.model.collection.CollectionItemModel

private val CARD_WIDTH = 270.dp
private val TITLE_HORIZONTAL_PADDING = 48.dp
private val DESCRIPTION_HORIZONTAL_PADDING = 34.dp

private val THUMBNAIL_HEIGHT = 202.dp

/**
 * 썸네일을 카드 배경으로 잦아들게 하는 그라데이션의 시작 지점.
 * 끝은 항상 썸네일 하단이라, 여기서 [THUMBNAIL_HEIGHT] 를 빼면 그라데이션 높이가 된다.
 */
private val MID_GRADIENT_TOP = 42.dp
private val BADGE_HEIGHT = 32.dp

/** 배지가 썸네일 위로 겹쳐 올라가는 높이. 배지는 178~210dp 구간에 놓인다. */
private val BADGE_OVERLAP = 24.dp
private val BADGE_TO_TITLE_SPACING = 16.dp
private val BOTTOM_GRADIENT_HEIGHT = 42.dp

@Composable
fun RecommendCollectionCard(
    item: CollectionItemModel,
    onItemClick: (id: String) -> Unit,
    isCurrentPage: Boolean,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isCurrentPage) FlintTheme.colors.primary900 else FlintTheme.colors.gray800
    val midGradient = if (isCurrentPage) FlintTheme.colors.blueGradient else FlintTheme.colors.grayGradient
    val bottomGradient = if (isCurrentPage) FlintTheme.colors.primary400Gradient else FlintTheme.colors.cardShadeGradient

    Box(
        modifier = modifier
            .width(CARD_WIDTH)
            .height(320.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .flintCardClickable { onItemClick(item.id) },
    ) {
        NetworkImage(
            imageUrl = item.thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(THUMBNAIL_HEIGHT),
        )

        Box(
            modifier = Modifier
                .padding(top = MID_GRADIENT_TOP)
                .fillMaxWidth()
                .height(THUMBNAIL_HEIGHT - MID_GRADIENT_TOP)
                .background(midGradient),
        )

        // 배지가 썸네일 하단에 걸치도록 상단 기준으로 배치한다.
        // 하단 기준으로 두면 제목/소개글이 한 줄일 때 텍스트 블록 전체가 아래로 밀린다.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = THUMBNAIL_HEIGHT - BADGE_OVERLAP),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .height(BADGE_HEIGHT)
                    .clip(RoundedCornerShape(16.dp))
                    .background(brush = FlintTheme.colors.userBadgeGradient)
                    .border(width = 0.5.dp, brush = FlintTheme.colors.userBadgeStroke, shape = RoundedCornerShape(16.dp))
                    .padding(top = 4.dp, bottom = 4.dp, start = 6.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ProfileImage(
                    imageUrl = item.profileUrl,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
                Text(
                    text = item.nickname,
                    style = FlintTheme.typography.caption1R12,
                    color = FlintTheme.colors.gray200,
                    maxLines = 1,
                )
            }

            Spacer(Modifier.height(BADGE_TO_TITLE_SPACING))

            CenteredEllipsisText(
                text = item.title,
                style = FlintTheme.typography.head3Sb18,
                color = FlintTheme.colors.gray50,
                maxLines = 2,
                horizontalPadding = TITLE_HORIZONTAL_PADDING,
            )

            Spacer(Modifier.height(4.dp))

            CenteredEllipsisText(
                text = item.description,
                style = FlintTheme.typography.caption1R12,
                color = FlintTheme.colors.gray200,
                maxLines = 2,
                horizontalPadding = DESCRIPTION_HORIZONTAL_PADDING,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(BOTTOM_GRADIENT_HEIGHT)
                .align(Alignment.BottomCenter)
                .background(bottomGradient)
        )
    }
}

private const val ELLIPSIS = "…"

/**
 * 가운데 정렬(TextAlign.Center)과 말줄임(TextOverflow.Ellipsis)을 함께 주면,
 * 생략된 마지막 줄이 가운데가 아니라 오른쪽 끝에 붙는다.
 * 2줄짜리 소개글에서 아랫줄만 들여쓰기된 것처럼 보이던 QA 제보가 이 때문이었다.
 *
 * 그래서 말줄임을 Compose 에 맡기지 않고, 들어갈 만큼 직접 잘라 말줄임표를 붙인 뒤
 * Clip 으로 그린다. 잘라낸 뒤에는 모든 줄이 같은 기준으로 가운데 정렬된다.
 */
@Composable
private fun CenteredEllipsisText(
    text: String,
    style: TextStyle,
    color: Color,
    maxLines: Int,
    horizontalPadding: Dp,
) {
    val measurer = rememberTextMeasurer()
    // 카드 폭이 고정이라 사용 가능한 폭도 고정이다. BoxWithConstraints 없이 바로 계산한다.
    val availableWidth = with(LocalDensity.current) { (CARD_WIDTH - horizontalPadding * 2).roundToPx() }

    val shownText = remember(text, style, maxLines, availableWidth) {
        text.truncateToFit(
            maxLines = maxLines,
            maxWidthPx = availableWidth,
            measurer = measurer,
            style = style,
        )
    }

    Text(
        text = shownText,
        style = style,
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Clip,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
    )
}

/** [maxLines] 안에 들어가는 가장 긴 앞부분을 이분 탐색으로 찾아 말줄임표를 붙인다. */
private fun String.truncateToFit(
    maxLines: Int,
    maxWidthPx: Int,
    measurer: TextMeasurer,
    style: TextStyle,
): String {
    fun overflows(candidate: String): Boolean =
        measurer.measure(
            text = AnnotatedString(candidate),
            style = style,
            maxLines = maxLines,
            constraints = Constraints(maxWidth = maxWidthPx),
        ).hasVisualOverflow

    if (!overflows(this)) return this

    var low = 0
    var high = length
    while (low < high) {
        val mid = (low + high + 1) / 2
        if (overflows(take(mid).trimEnd() + ELLIPSIS)) high = mid - 1 else low = mid
    }
    return take(low).trimEnd() + ELLIPSIS
}

@Preview
@Composable
private fun RecommendCollectionCardPreview() {
    FlintTheme {
        RecommendCollectionCard(
            item = CollectionItemModel(
                id = "1",
                thumbnailUrl = null,
                title = "추천 컬렉션 제목",
                description = "추천 컬렉션에 대한 설명입니다. 여러 줄일 경우 어떻게 보이는지 확인하기 위해 길게 작성합니다.",
                nickname = "작성자 닉네임",
                profileUrl = null
            ),
            isCurrentPage = true,
            onItemClick = {}
        )
    }
}

@Preview
@Composable
private fun RecommendCollectionCardInactivePreview() {
    FlintTheme {
        RecommendCollectionCard(
            item = CollectionItemModel(
                id = "3",
                thumbnailUrl = null,
                title = "사랑에 빠지기 10초 전",
                description = "시간이 흘러도 빛이 바래지 않는,사랑의 미묘한 온도를 담은 제 최애 영화 모음집입니다",
                nickname = "얀비",
                profileUrl = null
            ),
            isCurrentPage = false,
            onItemClick = {}
        )
    }
}

@Preview
@Composable
private fun RecommendCollectionCardSingleLinePreview() {
    FlintTheme {
        RecommendCollectionCard(
            item = CollectionItemModel(
                id = "2",
                thumbnailUrl = null,
                title = "사랑에 빠지기 10초 전",
                description = "한 줄짜리 소개글입니다",
                nickname = "얀비",
                profileUrl = null
            ),
            isCurrentPage = true,
            onItemClick = {}
        )
    }
}
