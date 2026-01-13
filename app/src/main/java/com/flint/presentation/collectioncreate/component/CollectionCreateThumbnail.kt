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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionCreateThumbnail(
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (imageUrl.isNullOrBlank()) {
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
                .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) { 
        Image(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
            painter = painterResource(R.drawable.img_collection_bg1),
            contentDescription = null,
        )

        Icon(
            modifier =
                Modifier
                    .align(Alignment.Center),
            painter = painterResource(R.drawable.ic_background_photo),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

@Composable
private fun CollectionCreateFillThumbnail(
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(1.5f / 1f)
                .noRippleClickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        NetworkImage(
            imageUrl = imageUrl,
            modifier =
                Modifier
                    .fillMaxWidth(),
        )

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(FlintTheme.colors.imgBlur),
        )

        Icon(
            modifier =
                Modifier
                    .align(Alignment.Center),
            painter = painterResource(R.drawable.ic_background_photo),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

@Preview
@Composable
fun CollectionCreateEmptyThumbnailPreview() {
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
