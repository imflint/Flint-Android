package com.flint.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.topappbar.FlintLogoTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.AuthorModel
import com.flint.domain.model.CollectionModel
import com.flint.domain.type.UserRoleType
import com.flint.presentation.home.component.HomeBanner
import com.flint.presentation.home.component.HomeRecommendCollection

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
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
private fun HomeScreen(
    userName: String = "",
    collectionModelList: List<CollectionModel> = emptyList(),
    onRecommendCollectionItemClick: (collectionId: String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(FlintTheme.colors.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                FlintLogoTopAppbar()
            }

            item {
                Spacer(Modifier.height(5.dp))

                HomeBanner(
                    userName = userName
                )
            }

            item {
                Spacer(Modifier.height(48.dp))

                HomeRecommendCollection(
                    collectionModelList = collectionModelList,
                    onItemClick = { collectionId ->
                        onRecommendCollectionItemClick(collectionId)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    FlintTheme {
        val collectionModelList = listOf(
            CollectionModel(
                collectionId = "",
                collectionTitle = "컬렉션 제목",
                collectionImageUrl = "",
                createdAt = "",
                isBookmarked = false,
                author = AuthorModel(
                    userId = 0,
                    nickname = "사용자 이름",
                    profileUrl = "",
                    userRole = UserRoleType.FLINER
                )
            ),
            CollectionModel(
                collectionId = "",
                collectionTitle = "컬렉션 제목2",
                collectionImageUrl = "",
                createdAt = "",
                isBookmarked = false,
                author = AuthorModel(
                    userId = 0,
                    nickname = "사용자 이름2",
                    profileUrl = "",
                    userRole = UserRoleType.FLINER
                )
            ),
        )


        HomeScreen(
            userName = "종우",
            collectionModelList = collectionModelList,
        )
    }
}