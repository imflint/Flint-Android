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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.dropShadow
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.component.image.ProfileImage
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionFileItem(
    profileImageUrl: String,
    nickname: String,
    isBookmarked: Boolean,
    bookmarkCount: Int,
    poster1Url: String,
    poster2Url: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 154.dp, height = 154.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(FlintTheme.colors.gray800)
    ) {
        CollectionPocketItem(
            imageUrl = poster1Url,
            modifier = Modifier
                .offset(x = 16.dp, y = 8.dp)
        )

        CollectionPocketItem(
            imageUrl = poster2Url,
            modifier = Modifier.dropShadow(
                shape = CircleShape,
                color = Color(0xFF000000).copy(alpha = 0.30f),
                offsetX = (-4).dp,
                offsetY = 0.dp,
                blur = 10.dp,
                spread = 0.dp
            )
                .align(Alignment.TopCenter)
                .rotate(15f)
                .offset(x = 20.dp, y = 15.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(108.dp)
                .align(Alignment.BottomEnd)
                .paint(
                    painter = painterResource(id = R.drawable.img_folder_fg),
                    contentScale = ContentScale.FillWidth
                )
                .padding(12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .align(Alignment.TopEnd)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        imageVector =
                            ImageVector.vectorResource(
                                if (isBookmarked) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_empty,
                            ),
                            contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        "$bookmarkCount",
                        style = TextStyle(
                            color = Color.White
                        ),
                        modifier = Modifier
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
            ) {
                ProfileImage(
                    imageUrl = profileImageUrl,
                    modifier = Modifier
                        .size(28.dp)
                )
                Text(
                    nickname,
                    style = FlintTheme.typography.caption1M12,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun CollectionPocketItem(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    NetworkImage(
        imageUrl = imageUrl,
        modifier = modifier
            .size(width = 80.dp, height = 120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
    )
}

@Preview(showBackground = false)
@Composable
private fun CollectionFileItemPreview() {
    FlintTheme {
        CollectionFileItem(
            profileImageUrl = "",
            nickname = "Flint",
            isBookmarked = true,
            bookmarkCount = 123,
            modifier = Modifier.padding(16.dp),
            poster1Url = "",
            poster2Url = ""
        )
    }
}
