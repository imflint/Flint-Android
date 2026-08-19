package com.flint.android.presentation.collectioncreate.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.android.R
import com.flint.android.core.designsystem.component.image.NetworkImage
import com.flint.android.core.designsystem.interaction.flintIconClickable
import com.flint.android.core.designsystem.theme.FlintTheme

@Composable
fun CollectionCreateContentImage(
    existingImageUrls: List<String>,
    imageUris: List<Uri>,
    onDeleteExistingClick: (index: Int) -> Unit,
    onDeleteClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val images: List<Any> = existingImageUrls + imageUris
    if (images.isEmpty()) return

    val pageCount = Int.MAX_VALUE
    val pagerState = rememberPagerState(
        initialPage = pageCount / 2 - (pageCount / 2) % images.size,
    ) { pageCount }
    val currentIndex = pagerState.currentPage % images.size

    var prevSize by remember { mutableIntStateOf(-1) }
    var deletedIndex by remember { mutableIntStateOf(-1) }

    LaunchedEffect(imageUris.size) {
        if (prevSize == -1) {
            prevSize = imageUris.size
            return@LaunchedEffect
        }
        if (imageUris.isEmpty()) {
            prevSize = 0
            return@LaunchedEffect
        }
        val isAdded = imageUris.size > prevSize
        val targetIndex = if (isAdded) {
            imageUris.size - 1
        } else {
            minOf(deletedIndex, imageUris.size - 1)
        }
        prevSize = imageUris.size
        // 새로운 size 기준으로 targetIndex에 해당하는 가장 가까운 절대 페이지 계산
        val base = (pagerState.currentPage / imageUris.size) * imageUris.size
        val targetPage = base + targetIndex
        if (isAdded) {
            pagerState.animateScrollToPage(targetPage)
        } else {
            pagerState.scrollToPage(targetPage)
        }
    }

    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = images.size > 1,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            val index = page % images.size
            Box {
                NetworkImage(
                    imageUrl = images[index],
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(360f / 270f)
                        .background(FlintTheme.colors.gray800),
                )
                Icon(
                    painter = painterResource(R.drawable.ic_deselect_large_gray),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .flintIconClickable {
                            deletedIndex = index
                            if (index < existingImageUrls.size) {
                                onDeleteExistingClick(index)
                            } else {
                                onDeleteClick(index - existingImageUrls.size)
                            }
                        }
                        .padding(all = 16.dp)
                        .size(24.dp),
                )
            }
        }

        if (images.size > 1) {
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            ) {
                repeat(images.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == currentIndex) FlintTheme.colors.secondary400
                                else FlintTheme.colors.gray500
                            ),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun CollectionCreateContentImagePreview() {
    FlintTheme {
        CollectionCreateContentImage(
            existingImageUrls = listOf("https://example.com/1"),
            imageUris = listOf(
                Uri.parse("https://example.com/2"),
                Uri.parse("https://example.com/3"),
            ),
            onDeleteExistingClick = {},
            onDeleteClick = {},
        )
    }
}
