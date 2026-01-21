package com.flint.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.core.common.util.UiState
import com.flint.core.designsystem.component.listView.CollectionSection
import com.flint.core.designsystem.component.listView.SavedContentsSection
import com.flint.core.designsystem.component.topappbar.FlintLogoTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.content.BookmarkedContentListModel
import com.flint.core.navigation.model.CollectionListRouteType
import com.flint.presentation.home.component.HomeBanner
import com.flint.presentation.home.component.HomeFab
import com.flint.presentation.home.component.HomeRecentCollectionEmpty

@Composable
fun HomeRoute(
    paddingValues: PaddingValues,
    navigateToCollectionList: (routeType: CollectionListRouteType) -> Unit,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
    navigateToCollectionCreate: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getRecommendedCollectionList()
        viewModel.getBookmarkedContentList()
        viewModel.getRecentCollectionList()
    }

    when (uiState.loadState) {
        is UiState.Success -> {
            val recommendedCollectionList = (uiState.recommendedCollectionListLoadState as? UiState.Success)?.data ?: CollectionListModel()
            val bookmarkedContentList = (uiState.bookmarkedContentListLoadState as? UiState.Success)?.data ?: BookmarkedContentListModel()
            val recentCollectionList = (uiState.recentCollectionListLoadState as? UiState.Success)?.data ?: CollectionListModel()

            HomeScreen(
                recommendCollectionModelList = recommendedCollectionList,
                recentCollectionModelList = recentCollectionList,
                savedContentModelList = bookmarkedContentList,
                navigateToCollectionCreate = {
                    navigateToCollectionCreate()
                },
                navigateToExplore = {
                    // TODO navigate to explore
                },
                onRecentCollectionItemClick = { collectionId ->
                    navigateToCollectionDetail(collectionId)
                },
                onRecentCollectionAllClick = { navigateToCollectionList(CollectionListRouteType.RECENT) },
                onRecommendCollectionItemClick = { collectionId ->
                    navigateToCollectionDetail(collectionId)
                },
                onSavedContentItemClick = { contentId ->
                    // TODO show OttListBottomSheet
                },
                modifier = Modifier.padding(paddingValues),
            )
        }
        else -> {}
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    userName: String = "",
    recommendCollectionModelList: CollectionListModel,
    savedContentModelList: BookmarkedContentListModel,
    recentCollectionModelList: CollectionListModel,
    onRecommendCollectionItemClick: (collectionId: String) -> Unit,
    onSavedContentItemClick: (contentId: String) -> Unit,
    onRecentCollectionItemClick: (collectionId: String) -> Unit,
    onRecentCollectionAllClick: () -> Unit,
    navigateToExplore: () -> Unit,
    navigateToCollectionCreate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(FlintTheme.colors.background)
                .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            overscrollEffect = null,
            contentPadding = PaddingValues(bottom = 80.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            stickyHeader {
                FlintLogoTopAppbar()
            }

            item {
                Spacer(Modifier.height(5.dp))

                HomeBanner(
                    userName = userName,
                )
            }

            item {
                Spacer(Modifier.height(48.dp))

                CollectionSection(
                    title = "Fliner의 추천 컬렉션을 만나보세요",
                    description = "Fliner는 콘텐츠에 진심인, 플린트의 큐레이터들이에요",
                    isAllVisible = false,
                    onAllClick = {},
                    collectionListModel = recommendCollectionModelList,
                    onItemClick = onRecommendCollectionItemClick,
                )
            }

            item {
                Spacer(Modifier.height(48.dp))

                SavedContentsSection(
                    title = "최근 저장한 콘텐츠",
                    description = "현재 구독 중인 OTT에서 볼 수 있는 작품들이에요",
                    isAllVisible = false,
                    onAllClick = {},
                    contentModelList = savedContentModelList,
                    onItemClick = onSavedContentItemClick,
                )
            }

            item {
                Spacer(Modifier.height(48.dp))

                if (recentCollectionModelList.collections.isEmpty()) {
                    HomeRecentCollectionEmpty(navigateToExplore = navigateToExplore)
                } else {
                    CollectionSection(
                        title = "눈여겨보고 있는 컬렉션",
                        description = "${userName}님이 최근 살펴본 컬렉션이에요",
                        isAllVisible = true,
                        onAllClick = onRecentCollectionAllClick,
                        collectionListModel = recentCollectionModelList,
                        onItemClick = onRecentCollectionItemClick,
                    )
                }
            }
        }

        HomeFab(
            onClick = navigateToCollectionCreate,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 8.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    FlintTheme {
        val collectionModelList = CollectionListModel.FakeList
        val contentModelList = BookmarkedContentListModel.FakeList

        HomeScreen(
            userName = "종우",
            recommendCollectionModelList = collectionModelList,
            savedContentModelList = contentModelList,
            recentCollectionModelList = collectionModelList,
            onRecommendCollectionItemClick = {},
            onSavedContentItemClick = {},
            onRecentCollectionItemClick = {},
            onRecentCollectionAllClick = {},
            navigateToExplore = {},
            navigateToCollectionCreate = {},
        )
    }
}
