package com.flint.presentation.collectioncreate.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.interaction.flintCardClickable
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionCreateThumbnail(
    imageUrl: Any?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (imageUrl == null || (imageUrl is String && imageUrl.isBlank())) {
        CollectionCreateEmptyThumbnail(
            onClick = onClick,
            modifier = modifier,
        )
    } else {
        CollectionCreateFillThumbnail(
            imageUrl = imageUrl,
            onClick = onClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun CollectionCreateEmptyThumbnail(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(360f / 140f)
                .flintCardClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.img_collection_bg1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Icon(
            painter = painterResource(R.drawable.ic_background_photo),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

@Composable
private fun CollectionCreateFillThumbnail(
    imageUrl: Any,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(360f / 240f)
                .flintCardClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        NetworkImage(
            imageUrl = imageUrl,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(FlintTheme.colors.imgBlur),
        )

        Icon(
            painter = painterResource(R.drawable.ic_background_photo),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

@Preview
@Composable
fun CollectionCreateThumbnailPreview() {
    FlintTheme {
        Column {
            CollectionCreateThumbnail(
                imageUrl = "https://buly.kr/DEaVFRZ",
                onClick = {},
            )

            Spacer(Modifier.height(20.dp))

            CollectionCreateThumbnail(
                imageUrl = null,
                onClick = {},
            )
        }
    }
}
