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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.flint.android.core.designsystem.interaction.flintCardClickable
import com.flint.android.core.designsystem.component.image.NetworkImage
import com.flint.android.core.designsystem.component.image.ProfileImage
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.model.collection.CollectionItemModel

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
            .width(270.dp)
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

            Text(
                text = item.title,
                style = FlintTheme.typography.head3Sb18,
                color = FlintTheme.colors.gray50,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = item.description,
                style = FlintTheme.typography.caption1R12,
                color = FlintTheme.colors.gray200,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp)
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
