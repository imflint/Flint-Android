package com.flint.android.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.model.collection.CollectionListModel

private val CARD_WIDTH = 270.dp
private const val INFINITE_PAGE_COUNT = Int.MAX_VALUE

@Composable
fun HomeRecommendCollectionList(
    collectionListModel: CollectionListModel,
    onItemClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (collectionListModel.collections.isEmpty()) return

    val actualCount = collectionListModel.collections.size

    // 초기 페이지를 중간 아이템으로 설정하면서 양방향 무한 스크롤 가능하도록 중앙 정렬
    val half = INFINITE_PAGE_COUNT / 2
    val initialPage = half - (half % actualCount) + (actualCount / 2)

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { INFINITE_PAGE_COUNT },
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Fliner의 추천 컬렉션",
                style = FlintTheme.typography.head3Sb18,
                color = FlintTheme.colors.white,
            )
            Text(
                text = "Fliner는 콘텐츠에 진심인, 플린트의 큐레이터들이에요",
                style = FlintTheme.typography.body2R14,
                color = FlintTheme.colors.gray100,
            )
        }

        Spacer(Modifier.height(24.dp))

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val horizontalPadding = (maxWidth - CARD_WIDTH) / 2
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fixed(CARD_WIDTH),
                pageSpacing = 12.dp,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                modifier = Modifier.fillMaxWidth(),
            ) { page ->
                val actualIndex = page % actualCount
                RecommendCollectionCard(
                    item = collectionListModel.collections[actualIndex],
                    isCurrentPage = page == pagerState.currentPage,
                    onItemClick = onItemClick,
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        val currentIndex = pagerState.currentPage % actualCount
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        ) {
            repeat(actualCount) { index ->
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

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun HomeRecommendCollectionListPreview() {
    FlintTheme {
        HomeRecommendCollectionList(
            collectionListModel = CollectionListModel.FakeList,
            onItemClick = {},
        )
    }
}
