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
    val pageCount = if (imageUris.size > 1) Int.MAX_VALUE else imageUris.size
    val initialPage = if (imageUris.size > 1) Int.MAX_VALUE / 2 else 0
    val pagerState = rememberPagerState(initialPage = initialPage) { pageCount }
    val currentIndex = if (imageUris.isEmpty()) 0 else pagerState.currentPage % imageUris.size

    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            val index = page % imageUris.size
            Box {
                NetworkImage(
                    imageUrl = imageUris[index],
                    contentScale = ContentScale.Fit,
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
                        .clickable { onDeleteClick(index) }
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
