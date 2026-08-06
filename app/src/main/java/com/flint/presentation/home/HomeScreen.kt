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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.core.common.util.UiState
import com.flint.core.designsystem.component.bottomsheet.OttListBottomSheet
import com.flint.core.designsystem.component.listView.CollectionSection
import com.flint.core.designsystem.component.listView.SavedContentsSection
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.content.BookmarkedContentListModel
import com.flint.domain.model.ott.OttListModel
import com.flint.core.navigation.model.CollectionListRouteType
import com.flint.presentation.home.component.HomeBanner
import com.flint.presentation.home.component.HomeFab
import com.flint.presentation.home.component.HomeRecommendCollectionList
import com.flint.presentation.home.sideeffect.HomeSideEffect

@OptIn(ExperimentalMaterial3Api::class)
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

    var showOttListBottomSheet by remember { mutableStateOf(false) }
    var ottListModel by remember { mutableStateOf(OttListModel()) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.getRecommendedCollectionList()
        viewModel.getBookmarkedContentList()
        viewModel.getPopularCollectionList()
    }

    LaunchedEffect(Unit) {
        viewModel.homeSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is HomeSideEffect.ShowOttListBottomSheet -> {
                    ottListModel = sideEffect.ottListModel
                    if (ottListModel.otts.isNotEmpty()) showOttListBottomSheet = true
                }
            }
        }
    }

    when (uiState.loadState) {
        is UiState.Success -> {
            val recommendedCollectionList = (uiState.recommendedCollectionListLoadState as? UiState.Success)?.data ?: CollectionListModel()
            val bookmarkedContentList = (uiState.bookmarkedContentListLoadState as? UiState.Success)?.data ?: BookmarkedContentListModel()
            val popularCollectionList = (uiState.popularCollectionListLoadState as? UiState.Success)?.data ?: CollectionListModel()

            HomeScreen(
                userName = uiState.userName,
                recommendCollectionModelList = recommendedCollectionList,
                famousCollectionModelList = popularCollectionList,
                savedContentModelList = bookmarkedContentList,
                navigateToCollectionCreate = { navigateToCollectionCreate() },
                onFamousCollectionItemClick = { navigateToCollectionDetail(it) },
                onFamousCollectionAllClick = { navigateToCollectionList(CollectionListRouteType.FAMOUS) },
                onRecommendCollectionItemClick = { navigateToCollectionDetail(it) },
                onSavedContentItemClick = { viewModel.showOttList(it) },
                modifier = Modifier.padding(paddingValues),
            )
        }
        else -> {}
    }

    if (showOttListBottomSheet) {
        OttListBottomSheet(
            ottList = ottListModel,
            onDismiss = { showOttListBottomSheet = false },
            sheetState = sheetState,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    userName: String,
    recommendCollectionModelList: CollectionListModel,
    savedContentModelList: BookmarkedContentListModel,
    famousCollectionModelList: CollectionListModel,
    onRecommendCollectionItemClick: (collectionId: String) -> Unit,
    onSavedContentItemClick: (contentId: String) -> Unit,
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

            if (savedContentModelList.contents.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(42.dp))

                    SavedContentsSection(
                        title = "최근 저장한 콘텐츠",
                        description = "현재 구독 중인 OTT에서 볼 수 있는 작품들이에요",
                        isAllVisible = false,
                        onAllClick = {},
                        contentModelList = savedContentModelList,
                        onItemClick = onSavedContentItemClick,
                    )
                }
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
        HomeScreen(
            userName = "종우",
            recommendCollectionModelList = CollectionListModel.FakeList,
            savedContentModelList = BookmarkedContentListModel.FakeList,
            famousCollectionModelList = CollectionListModel.FakeList,
            onRecommendCollectionItemClick = {},
            onSavedContentItemClick = {},
            onFamousCollectionItemClick = {},
            onFamousCollectionAllClick = {},
            navigateToCollectionCreate = {},
        )
    }
}
