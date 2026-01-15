package com.flint.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.topappbar.FlintLogoTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.CollectionModel
import com.flint.domain.model.ContentModel
import com.flint.presentation.home.component.HomeBanner
import com.flint.presentation.home.component.HomeFab
import com.flint.presentation.home.component.HomeRecentCollection
import com.flint.presentation.home.component.HomeRecentCollectionEmpty
import com.flint.presentation.home.component.HomeRecommendCollection
import com.flint.presentation.home.component.HomeSavedContents

@Composable
fun HomeRoute(
    paddingValues: PaddingValues,
    navigateToCollectionList: () -> Unit,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
    navigateToCollectionCreate: () -> Unit,
) {
    HomeScreen(
        onRecommendCollectionItemClick = { collectionId ->
            navigateToCollectionDetail(collectionId)
        },
        onSavedContentItemClick = { contentId ->
            // TODO show OttListBottomSheet
        },
        modifier = Modifier.padding(paddingValues),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    userName: String = "",
    recommendCollectionModelList: List<CollectionModel> = emptyList(),
    savedContentModelList: List<ContentModel> = emptyList(),
    recentCollectionModelList: List<CollectionModel> = emptyList(),
    onRecommendCollectionItemClick: (collectionId: String) -> Unit = {},
    onSavedContentItemClick: (contentId: Long) -> Unit = {},
    onRecentCollectionItemClick: (collectionId: String) -> Unit = {},
    onRecentCollectionAllClick: () -> Unit = {},
    navigateToExplore: () -> Unit = {},
    navigateToCollectionCreate: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(FlintTheme.colors.background)
                .fillMaxSize()
                .statusBarsPadding(),
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

                HomeRecommendCollection(
                    collectionModelList = recommendCollectionModelList,
                    onItemClick = { collectionId ->
                        onRecommendCollectionItemClick(collectionId)
                    },
                )
            }

            item {
                Spacer(Modifier.height(48.dp))

                HomeSavedContents(
                    contentModelList = savedContentModelList,
                    onItemClick = { contentId ->
                        onSavedContentItemClick(contentId)
                    },
                )
            }

            item {
                Spacer(Modifier.height(48.dp))

                if (recentCollectionModelList.isEmpty()) {
                    HomeRecentCollectionEmpty(navigateToExplore = navigateToExplore)
                } else {
                    HomeRecentCollection(
                        userName = userName,
                        collectionModelList = recentCollectionModelList,
                        onItemClick = { collectionId ->
                            onRecentCollectionItemClick(collectionId)
                        },
                        onAllClick = {
                            onRecentCollectionAllClick()
                        },
                    )
                }
            }
        }

        /** FAB **/
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
        val collectionModelList = CollectionModel.FakeList
        val contentModelList = ContentModel.FakeList

        HomeScreen(
            userName = "종우",
            recommendCollectionModelList = collectionModelList,
            savedContentModelList = contentModelList,
            recentCollectionModelList = collectionModelList, // or 'emptyList()'
        )
    }
}
