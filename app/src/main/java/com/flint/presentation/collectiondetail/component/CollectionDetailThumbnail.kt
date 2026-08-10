package com.flint.presentation.collectiondetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.component.button.FlintSaveDoneButton
import com.flint.core.designsystem.component.button.FlintSaveNoneButton
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionDetailThumbnail(
    thumbnailImage: String?,
    title: String,
    isBookmarked: Boolean,
    onSaveDoneButtonClick: () -> Unit,
    onSaveNoneButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
                .fillMaxWidth()
                .aspectRatio(360f / 270f),
    ) {
        if (thumbnailImage.isNullOrBlank()) {
            Image(
                painter = painterResource(R.drawable.img_collection_bg1),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            NetworkImage(
                imageUrl = thumbnailImage,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }

        Box(
            modifier = Modifier
                    .fillMaxSize()
                    .background(FlintTheme.colors.thumbnailGradient),
        )

        Column(
            modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 57.dp, bottom = 22.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.display2M28,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (isBookmarked) {
                FlintSaveDoneButton(
                    onClick = {
                        onSaveDoneButtonClick()
                    },
                )
            } else {
                FlintSaveNoneButton(
                    onClick = {
                        onSaveNoneButtonClick()
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun ThumbnailPreview() {
    FlintTheme {
        Column {
            CollectionDetailThumbnail(
                thumbnailImage = "",
                title = "한번 보면 못 빠져나오는 여운남는 사랑이야기".repeat(2),
                isBookmarked = true,
                onSaveDoneButtonClick = {},
                onSaveNoneButtonClick = {},
            )

            Spacer(Modifier.height(20.dp))

            CollectionDetailThumbnail(
                thumbnailImage = "https://buly.kr/DEaVFRZ",
                title = "한번 보면 못 빠져나오는 여운남는 사랑이야기",
                isBookmarked = false,
                onSaveDoneButtonClick = {},
                onSaveNoneButtonClick = {},
            )
        }
    }
}
