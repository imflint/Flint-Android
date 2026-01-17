package com.flint.core.designsystem.component.listItem

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.data.model.user.AuthorModel
import com.flint.data.model.collection.CollectionModel
import com.flint.core.common.type.UserRoleType

@Composable
fun CollectionItem(
    collectionModel: CollectionModel,
    onItemClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .width(260.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(FlintTheme.colors.gray200)
                .noRippleClickable {
                    onItemClick(collectionModel.collectionId)
                },
    ) {
        NetworkImage(
            imageUrl = collectionModel.collectionImageUrl,
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
            Image(
                painter =
                    if (collectionModel.author.profileUrl.isEmpty()) {
                        painterResource(R.drawable.ic_avatar_gray)
                    } else {
                        rememberAsyncImagePainter(collectionModel.author.profileUrl)
                    },
                contentDescription = null,
                modifier = Modifier.size(28.dp),
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    text = collectionModel.collectionTitle,
                    style = FlintTheme.typography.body2M14,
                    color = FlintTheme.colors.gray50,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    text = collectionModel.author.nickname,
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
        val collectionModel =
            CollectionModel(
                collectionId = "",
                collectionTitle = "컬렉션 제목",
                collectionImageUrl = "",
                createdAt = "",
                isBookmarked = false,
                author =
                    AuthorModel(
                        userId = 0,
                        nickname = "사용자 이름",
                        profileUrl = "",
                        userRole = UserRoleType.FLINER,
                    ),
            )

        CollectionItem(
            collectionModel = collectionModel,
            onItemClick = {},
        )
    }
}
