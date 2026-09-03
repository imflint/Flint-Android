package com.flint.android.core.designsystem.component.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.flint.android.R

/**
 * 이미지 비율과 컨테이너 비율의 편차가 이 값을 초과하면 원본 비율을 유지한 채 여백(Fit)을 두고,
 * 그렇지 않으면 컨테이너를 꽉 채우도록 Crop 한다.
 */
private const val ASPECT_RATIO_DEVIATION_THRESHOLD = 1.6f

/**
 * 이미지 원본 비율이 컨테이너 비율과 크게 다를 때만 여백(Fit)을 두고,
 * 그 외에는 컨테이너를 꽉 채우는(Crop) 이미지 컴포넌트.
 */
@Composable
fun AdaptiveScaleNetworkImage(
    imageUrl: Any?,
    letterboxColor: Color,
    modifier: Modifier = Modifier,
    containerHeight: Dp = 270.dp,
    placePainter: Painter = painterResource(R.drawable.img_network_loading),
    errorPainter: Painter = painterResource(R.drawable.img_network_loading),
    contentDescription: String? = null,
) {
    if (LocalInspectionMode.current) {
        NetworkImage(
            imageUrl = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
                    .fillMaxWidth()
                    .height(containerHeight),
        )
        return
    }

    BoxWithConstraints(
        modifier = modifier
                .fillMaxWidth()
                .height(containerHeight)
                .background(letterboxColor),
    ) {
        val containerRatio = maxWidth.value / containerHeight.value

        val painter = rememberAsyncImagePainter(
            model = imageUrl,
            placeholder = placePainter,
            error = errorPainter,
        )
        val painterState by painter.state.collectAsState()
        val successState = painterState as? AsyncImagePainter.State.Success
        val contentScale = successState?.let {
            val image = it.result.image
            if (image.width > 0 && image.height > 0) {
                val imageRatio = image.width.toFloat() / image.height.toFloat()
                val deviation = maxOf(imageRatio, containerRatio) / minOf(imageRatio, containerRatio)
                if (deviation > ASPECT_RATIO_DEVIATION_THRESHOLD) ContentScale.Fit else ContentScale.Crop
            } else {
                ContentScale.Crop
            }
        } ?: ContentScale.Crop

        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
