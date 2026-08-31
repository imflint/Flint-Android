package com.flint.android.presentation.home

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
import com.flint.android.core.analytics.CollectionSource
import com.flint.android.core.analytics.FlintEvent
import com.flint.android.core.analytics.HomeContentType
import com.flint.android.core.analytics.LocalAnalyticsTracker
import com.flint.android.core.analytics.TrackScreenView
import com.flint.android.core.common.util.UiState
import com.flint.android.core.designsystem.component.bottomsheet.OttListBottomSheet
import com.flint.android.core.designsystem.component.listView.CollectionSection
import com.flint.android.core.designsystem.component.listView.SavedContentsSection
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.model.collection.CollectionListModel
import com.flint.android.domain.model.content.BookmarkedContentListModel
import com.flint.android.domain.model.ott.OttListModel
import com.flint.android.core.navigation.model.CollectionListRouteType
import com.flint.android.presentation.home.component.HomeBanner
import com.flint.android.presentation.home.component.HomeFab
import com.flint.android.presentation.home.component.HomeRecommendCollectionList
import com.flint.android.presentation.home.sideeffect.HomeSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    paddingValues: PaddingValues,
    navigateToCollectionList: (routeType: CollectionListRouteType) -> Unit,
    navigateToCollectionDetail: (collectionId: String, source: CollectionSource) -> Unit,
    navigateToCollectionCreate: () -> Unit,
    navigateToExplore: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val analyticsTracker = LocalAnalyticsTracker.current

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

            // 정의서상 홈 진입은 "데이터 로딩 완료 후 정상 표시 시점" 이라 Success 분기에서 보낸다.
            TrackScreenView(FlintEvent.ViewHome)

            HomeScreen(
                userName = uiState.userName,
                recommendCollectionModelList = recommendedCollectionList,
                famousCollectionModelList = popularCollectionList,
                savedContentModelList = bookmarkedContentList,
                navigateToCollectionCreate = { navigateToCollectionCreate() },
                onFamousCollectionItemClick = {
                    analyticsTracker.track(FlintEvent.ClickHomeContent(HomeContentType.POPULAR))
                    navigateToCollectionDetail(it, CollectionSource.HOME_POPULAR)
                },
                onFamousCollectionAllClick = { navigateToCollectionList(CollectionListRouteType.FAMOUS) },
                onRecommendCollectionItemClick = {
                    analyticsTracker.track(FlintEvent.ClickHomeContent(HomeContentType.FLINER))
                    navigateToCollectionDetail(it, CollectionSource.HOME_FLINNER)
                },
                onSavedContentItemClick = {
                    analyticsTracker.track(FlintEvent.ClickHomeContent(HomeContentType.RECENTLY_SAVED))
                    viewModel.showOttList(it)
                },
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
                        description = "관심이 가는 작품들을 다시 만나보세요",
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
