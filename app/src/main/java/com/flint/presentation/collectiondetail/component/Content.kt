package com.flint.presentation.collectiondetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun Content(
    content: ContentModelNew,
    onBookmarkIconClick: (contentId: String) -> Unit,
    onSpoilClick: (contentId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        NetworkImage(
            imageUrl = content.imageUrl,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(360f / 480f),
            contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.height(32.dp))

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        content.title,
                        color = FlintTheme.colors.white,
                        style = FlintTheme.typography.head2Sb20,
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        content.year.toString(),
                        color = FlintTheme.colors.gray300,
                        style = FlintTheme.typography.body1R16,
                    )

                    Text(
                        content.director,
                        color = FlintTheme.colors.gray300,
                        style = FlintTheme.typography.body1R16,
                    )
                }

                Row(
                    modifier =
                        Modifier.noRippleClickable(onClick = { onBookmarkIconClick(content.id) }),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .padding(start = 24.dp)
                                .padding(vertical = 3.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
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
                            color = FlintTheme.colors.white,
                            style = FlintTheme.typography.caption1M12,
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(FlintTheme.colors.gray500),
            )

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
                        modifier =
                            Modifier
                                .defaultMinSize(minHeight = 183.dp)
                                .fillMaxWidth(),
                    )
                }
            } else {
                Text(
                    text = content.reason,
                    color = FlintTheme.colors.gray100,
                    style = FlintTheme.typography.body1R16,
                    modifier =
                        Modifier
                            .defaultMinSize(minHeight = 183.dp)
                            .fillMaxWidth(),
                )
            }

            Spacer(Modifier.height(64.dp))
        }
    }
}

private class ContentPreviewProvider : PreviewParameterProvider<ContentModelNew> {
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
private fun ContentPreview(
    @PreviewParameter(ContentPreviewProvider::class) content: ContentModelNew,
) {
    FlintTheme {
        Content(
            content = content,
            onBookmarkIconClick = {},
            onSpoilClick = {}
        )
    }
}
