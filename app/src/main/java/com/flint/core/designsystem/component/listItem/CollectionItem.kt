package com.flint.core.designsystem.component.listItem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.component.image.ProfileImage
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.collection.CollectionItemModel

@Composable
fun CollectionItem(
    collectionItemModel: CollectionItemModel,
    onItemClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .width(260.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .noRippleClickable {
                    onItemClick(collectionItemModel.id)
                },
    ) {
        NetworkImage(
            imageUrl = collectionItemModel.thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(FlintTheme.colors.imgBlur),
        )

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .rotate(180f)
                    .background(FlintTheme.colors.imgBlurHigh),
        )

        Row(
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 14.dp, bottom = 10.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileImage(
                imageUrl = collectionItemModel.profileUrl,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = collectionItemModel.title,
                    style = FlintTheme.typography.body2M14,
                    color = FlintTheme.colors.gray50,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    text = collectionItemModel.nickname,
                    style = FlintTheme.typography.caption1R12,
                    color = FlintTheme.colors.gray200,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCollectionItem() {
    FlintTheme {
        val collectionItemModel =
            CollectionItemModel(
                id = "0",
                thumbnailUrl = "",
                title = "드라마 제목",
                description = "드라마 제목 드라마 제목 드라마 제목 드라마 제목 드라마 제목",
                imageList = emptyList(),
                bookmarkCount = 0,
                isBookmarked = false,
                userId = "0",
                nickname = "nickname",
                profileUrl = null
            )

        CollectionItem(
            collectionItemModel = collectionItemModel,
            onItemClick = {},
        )
    }
}
