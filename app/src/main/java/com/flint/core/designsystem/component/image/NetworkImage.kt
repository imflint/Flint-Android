package com.flint.core.designsystem.component.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun NetworkImage(
    imageUrl: Any?,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null
) {
    if (LocalInspectionMode.current) { // 프리뷰 확인용
        Image(
            painter = painterResource(R.drawable.img_dummy_poster),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier.clip(shape)

        )
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = contentScale,
            error = ColorPainter(FlintTheme.colors.gray200),
            modifier = modifier.clip(shape)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NetworkImagePreview() {
    FlintTheme {
        Row {
            NetworkImage(
                imageUrl = "",
                modifier = Modifier.size(50.dp)
            )
            NetworkImage(
                imageUrl = "",
                shape = CircleShape,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}
