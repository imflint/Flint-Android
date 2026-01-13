package com.flint.presentation.collectiondetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.dropShadow
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.component.image.ProfileImage
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.AuthorModel
import com.flint.domain.model.CollectionDetailModel
import com.flint.domain.type.UserRoleType

@Composable
fun CollectionFileItem(
    collection: CollectionDetailModel,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier,
    ) {
        with(collection) {
            CollectionFileContent(
                profileImageUrl = author.profileUrl,
                nickname = author.nickname,
                isBookmarked = isBookmarked,
                bookmarkCount = bookmarkCount,
                poster1Url = collectionImageUrl1,
                poster2Url = collectionImageUrl2,
                modifier = Modifier.size(154.dp),
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.width(154.dp),
        ) {
            Text(
                text = collection.collectionTitle,
                style = FlintTheme.typography.body1M16,
                color = FlintTheme.colors.white,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = collection.collectionContent,
                style = FlintTheme.typography.caption1R12,
                color = FlintTheme.colors.gray300,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun CollectionFileContent(
    profileImageUrl: String,
    nickname: String,
    isBookmarked: Boolean,
    bookmarkCount: Int,
    poster1Url: String,
    poster2Url: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(12.dp))
                .background(FlintTheme.colors.gray800),
    ) {
        CollectionPocketPoster(
            imageUrl = poster1Url,
            modifier =
                Modifier
                    .offset(x = 16.dp, y = 8.dp),
        )

        CollectionPocketPoster(
            imageUrl = poster2Url,
            modifier =
                Modifier
                    .dropShadow(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF000000).copy(alpha = 0.35f),
                        offsetX = (-4).dp,
                        offsetY = 0.dp,
                        blur = 10.dp,
                        spread = 0.dp,
                    ).align(Alignment.TopCenter)
                    .rotate(15f)
                    .offset(x = 20.dp, y = 15.dp),
        )

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(108.dp)
                    .align(Alignment.BottomEnd)
                    .paint(
                        painter = painterResource(id = R.drawable.img_folder_fg),
                        contentScale = ContentScale.FillWidth,
                    ).padding(start = 12.dp, top = 12.dp, end = 5.dp, bottom = 9.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .align(Alignment.TopEnd),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.size(48.dp),
                ) {
                    Image(
                        imageVector =
                            ImageVector.vectorResource(
                                if (isBookmarked) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_empty,
                            ),
                        contentDescription = null,
                        modifier =
                            Modifier
                                .size(24.dp),
                    )
                    Text(
                        "$bookmarkCount",
                        color = FlintTheme.colors.gray50,
                        style = FlintTheme.typography.caption1M12,
                        modifier = Modifier,
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier =
                    Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(),
            ) {
                ProfileImage(
                    imageUrl = profileImageUrl,
                    modifier =
                        Modifier
                            .size(28.dp),
                )
                Text(
                    nickname,
                    style = FlintTheme.typography.caption1M12,
                    color = FlintTheme.colors.gray50,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun CollectionPocketPoster(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    NetworkImage(
        imageUrl = imageUrl,
        modifier =
            modifier
                .size(width = 80.dp, height = 120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White),
    )
}

@Preview(showBackground = false)
@Composable
private fun CollectionFileItemPreview() {
    FlintTheme {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CollectionFileItem(
                collection =
                    CollectionDetailModel(
                        collectionId = "1",
                        collectionTitle = "컬렉션 내용",
                        collectionContent = "컬렉션 내용",
                        collectionImageUrl1 = "",
                        collectionImageUrl2 = "",
                        author =
                            AuthorModel(
                                userId = 1,
                                nickname = "Nickname",
                                profileUrl = "",
                                userRole = UserRoleType.FLINER,
                            ),
                        isBookmarked = true,
                        bookmarkCount = 10,
                        createdAt = "",
                    ),
            )
            CollectionFileItem(
                collection =
                    CollectionDetailModel(
                        collectionId = "1",
                        collectionTitle = "컬렉션 제목이 마구 길어질 때 두줄까지 표시됩니다.",
                        collectionContent = "컬렉션 내용이 마구 길어질 때 두줄까지 표시됩니다. 내용이 작으니까 조금 더 표시",
                        collectionImageUrl1 = "",
                        collectionImageUrl2 = "",
                        author =
                            AuthorModel(
                                userId = 1,
                                nickname = "닉네임은여덟글자",
                                profileUrl = "",
                                userRole = UserRoleType.FLINER,
                            ),
                        isBookmarked = true,
                        bookmarkCount = 10,
                        createdAt = "",
                    ),
            )
        }
    }
}
