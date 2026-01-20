package com.flint.core.designsystem.component.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.flint.R

@Composable
fun ProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    if (imageUrl.isNullOrBlank()) {
        Image(
            painter = painterResource(R.drawable.ic_avatar_blue),
            contentDescription = contentDescription,
            modifier = modifier.clip(CircleShape),
        )
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_avatar_blue),
            error = painterResource(R.drawable.ic_avatar_blue),
            modifier = modifier.clip(shape = CircleShape),
        )
    }
}
