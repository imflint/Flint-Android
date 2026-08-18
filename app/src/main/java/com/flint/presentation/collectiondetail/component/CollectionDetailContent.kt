package com.flint.presentation.collectiondetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.component.collection.Spoiler
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.content.ContentModelNew

@Composable
fun CollectionDetailContent(
    content: ContentModelNew,
    onBookmarkIconClick: (contentId: String) -> Unit,
    onSpoilClick: (contentId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 카드 사이 36dp 여백은 배경색 밖에 둬서 화면 기본 배경(검정)이 그대로 보이게 한다.
        Spacer(Modifier.height(36.dp))

        Column(
            modifier = Modifier
                    .fillMaxWidth()
                    .background(FlintTheme.colors.subBackground),
        ) {
            if (content.customImageUrls.isNotEmpty()) {
                CollectionDetailContentCarousel(content = content)
            }

            CollectionDetailContentInfo(
                content = content,
                onBookmarkIconClick = onBookmarkIconClick,
                onSpoilClick = onSpoilClick,
            )
        }
    }
}

@Composable
private fun CollectionDetailContentCarousel(content: ContentModelNew) {
    val images = content.customImageUrls
    val pageCount = Int.MAX_VALUE
    val pagerState = rememberPagerState(
        initialPage = pageCount / 2 - (pageCount / 2) % images.size,
    ) { pageCount }
    val currentIndex = pagerState.currentPage % images.size

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = images.size > 1,
        modifier = Modifier.fillMaxWidth(),
    ) { page ->
        NetworkImage(
            imageUrl = images[page % images.size],
            modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(360f / 270f)
                    .background(FlintTheme.colors.gray800),
            contentScale = ContentScale.Fit,
        )
    }

    if (images.size > 1) {
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        ) {
            repeat(images.size) { index ->
                Box(
                    modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == currentIndex) {
                                    FlintTheme.colors.secondary400
                                } else {
                                    FlintTheme.colors.gray500
                                },
                            ),
                )
            }
        }
    }
}

@Composable
private fun CollectionDetailContentInfo(
    content: ContentModelNew,
    onBookmarkIconClick: (contentId: String) -> Unit,
    onSpoilClick: (contentId: String) -> Unit,
) {
    Column(
        modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row {
                NetworkImage(
                    imageUrl = content.imageUrl,
                    modifier = Modifier.size(width = 60.dp, height = 90.dp),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = content.title,
                        color = FlintTheme.colors.white,
                        style = FlintTheme.typography.head2Sb20,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = content.year.toString(),
                        color = FlintTheme.colors.gray300,
                        style = FlintTheme.typography.body1R16,
                    )

                    Text(
                        text = content.director,
                        color = FlintTheme.colors.gray300,
                        style = FlintTheme.typography.body1R16,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .noRippleClickable(onClick = { onBookmarkIconClick(content.id) })
                    .padding(top = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (content.isBookmarked) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_bookmark_fill),
                        contentDescription = "저장됨",
                        tint = Color.Unspecified,
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_bookmark_empty),
                        contentDescription = "저장되지 않음",
                        tint = Color.White,
                    )
                }

                Text(
                    text = content.bookmarkCount.toString(),
                    color = FlintTheme.colors.gray200,
                    style = FlintTheme.typography.caption1M12,
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        HorizontalDivider(color = FlintTheme.colors.gray500, thickness = 1.dp)

        Spacer(Modifier.height(24.dp))

        if (content.isSpoiler) {
            Spoiler(
                onSpoilClick = { onSpoilClick(content.id) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = content.reason,
                    color = FlintTheme.colors.gray100,
                    style = FlintTheme.typography.body1R16,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else {
            Text(
                text = content.reason,
                color = FlintTheme.colors.gray100,
                style = FlintTheme.typography.body1R16,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

private class CollectionDetailContentPreviewProvider : PreviewParameterProvider<ContentModelNew> {
    override val values: Sequence<ContentModelNew> =
        sequenceOf(
            ContentModelNew(
                id = "0",
                title = "드라마 제목",
                year = 2000,
                imageUrl = "",
                director = "가스 제닝스",
                reason = "달라진 온도\n-\n같은 구도에 채도를 달리해 변해버린 사랑을 시각적으로 담아낸 장면들",
                isSpoiler = false,
                isBookmarked = false,
                bookmarkCount = 42,
            ),
            ContentModelNew(
                id = "0",
                title = "스포일러 있는 영화",
                year = 2024,
                imageUrl = "",
                director = "감독 이름",
                reason = "이 내용은 스포일러가 포함되어 있습니다.",
                isSpoiler = true,
                isBookmarked = false,
                bookmarkCount = 42,
            ),
            ContentModelNew(
                id = "0",
                title = "저장된 영화",
                year = 2023,
                imageUrl = "",
                director = "다른 감독",
                reason = "내가 저장한 영화입니다.",
                isSpoiler = false,
                isBookmarked = true,
                bookmarkCount = 42,
            ),
        )
}

@Preview
@Composable
private fun CollectionDetailContentPreview(
    @PreviewParameter(CollectionDetailContentPreviewProvider::class) content: ContentModelNew,
) {
    FlintTheme {
        CollectionDetailContent(
            content = content,
            onBookmarkIconClick = {},
            onSpoilClick = {}
        )
    }
}
