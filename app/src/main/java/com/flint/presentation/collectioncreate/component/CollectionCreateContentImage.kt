package com.flint.presentation.collectioncreate.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.flint.R
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionCreateContentImage(
    imageUris: List<Uri>,
    onDeleteClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2) { Int.MAX_VALUE }
    val currentIndex = if (imageUris.isEmpty()) 0 else pagerState.currentPage % imageUris.size

    var prevSize by remember { mutableIntStateOf(imageUris.size) }
    var deletedIndex by remember { mutableIntStateOf(-1) }

    LaunchedEffect(imageUris.size) {
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
            userScrollEnabled = imageUris.size > 1,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            val index = page % imageUris.size
            Box {
                NetworkImage(
                    imageUrl = imageUris[index],
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f),
                )
                Icon(
                    painter = painterResource(R.drawable.ic_deselect_large_gray),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable {
                        deletedIndex = index
                        onDeleteClick(index)
                    }
                        .padding(all = 16.dp)
                        .size(24.dp),
                )
            }
        }

        if (imageUris.size > 1) {
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            ) {
                repeat(imageUris.size) { index ->
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
            imageUris = listOf(
                Uri.parse("https://example.com/1"),
                Uri.parse("https://example.com/2"),
                Uri.parse("https://example.com/3"),
            ),
            onDeleteClick = {},
        )
    }
}
