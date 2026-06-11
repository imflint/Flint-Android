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
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.collection.CollectionListModel
import com.flint.core.navigation.model.CollectionListRouteType
import com.flint.presentation.home.component.HomeBanner
import com.flint.presentation.home.component.HomeFab
import com.flint.presentation.home.component.HomeRecommendCollectionList

@Composable
fun HomeRoute(
    paddingValues: PaddingValues,
    navigateToCollectionList: (routeType: CollectionListRouteType) -> Unit,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
    navigateToCollectionCreate: () -> Unit,
    navigateToExplore: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getRecommendedCollectionList()
        viewModel.getBookmarkedCollectionList()
        viewModel.getPopularCollectionList()
    }

    when (uiState.loadState) {
        is UiState.Success -> {
            val recommendedCollectionList = (uiState.recommendedCollectionListLoadState as? UiState.Success)?.data ?: CollectionListModel()
            val bookmarkedCollectionList = (uiState.bookmarkedCollectionListLoadState as? UiState.Success)?.data ?: CollectionListModel()
            val popularCollectionList = (uiState.popularCollectionListLoadState as? UiState.Success)?.data ?: CollectionListModel()

            HomeScreen(
                userName = uiState.userName,
                recommendCollectionModelList = recommendedCollectionList,
                famousCollectionModelList = popularCollectionList,
                savedCollectionModelList = bookmarkedCollectionList,
                navigateToCollectionCreate = {
                    navigateToCollectionCreate()
                },
                onFamousCollectionItemClick = { collectionId ->
                    navigateToCollectionDetail(collectionId)
                },
                onFamousCollectionAllClick = { navigateToCollectionList(CollectionListRouteType.FAMOUS) },
                onRecommendCollectionItemClick = { collectionId ->
                    navigateToCollectionDetail(collectionId)
                },
                onSavedCollectionItemClick = { collectionId ->
                    navigateToCollectionDetail(collectionId)
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
    userName: String,
    recommendCollectionModelList: CollectionListModel,
    savedCollectionModelList: CollectionListModel,
    famousCollectionModelList: CollectionListModel,
    onRecommendCollectionItemClick: (collectionId: String) -> Unit,
    onSavedCollectionItemClick: (collectionId: String) -> Unit,
    onFamousCollectionItemClick: (collectionId: String) -> Unit,
    onFamousCollectionAllClick: () -> Unit,
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
            contentPadding = PaddingValues(bottom = 20.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Spacer(Modifier.height(5.dp))

                HomeBanner(
                    userName = userName,
                )
            }

            item {
                Spacer(Modifier.height(24.dp))

                HomeRecommendCollectionList(
                    collectionListModel = recommendCollectionModelList,
                    onItemClick = onRecommendCollectionItemClick,
                )
            }

            item {
                Spacer(Modifier.height(42.dp))

                CollectionSection(
                    title = "저장한 컬렉션",
                    description = "내가 저장한 컬렉션들이에요",
                    isAllVisible = false,
                    onAllClick = {},
                    collectionListModel = savedCollectionModelList,
                    onItemClick = onSavedCollectionItemClick,
                )
            }

            item {
                Spacer(Modifier.height(42.dp))

                CollectionSection(
                    title = "인기 컬렉션",
                    description = "사람들이 눈여겨보는 컬렉션들이에요",
                    isAllVisible = true,
                    onAllClick = onFamousCollectionAllClick,
                    collectionListModel = famousCollectionModelList,
                    onItemClick = onFamousCollectionItemClick,
                )
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

        HomeScreen(
            userName = "종우",
            recommendCollectionModelList = collectionModelList,
            savedCollectionModelList = collectionModelList,
            famousCollectionModelList = collectionModelList,
            onRecommendCollectionItemClick = {},
            onSavedCollectionItemClick = {},
            onFamousCollectionItemClick = {},
            onFamousCollectionAllClick = {},
            navigateToCollectionCreate = {},
        )
    }
}
