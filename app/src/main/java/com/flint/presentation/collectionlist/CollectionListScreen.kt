package com.flint.presentation.collectionlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.common.util.UiState
import com.flint.core.designsystem.component.indicator.FlintLoadingIndicator
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.collection.CollectionListModel
import com.flint.presentation.collectionlist.component.CollectionFileItem

@Composable
fun CollectionListRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
    viewModel: CollectionListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectionListScreen(
        modifier = Modifier.padding(paddingValues),
        title = viewModel.routeType.title,
        onBackClick = navigateUp,
        onCollectionItemClick = navigateToCollectionDetail,
        collectionList = uiState.collectionList,
    )
}

@Composable
private fun CollectionListScreen(
    onBackClick: () -> Unit,
    title: String,
    collectionList: UiState<CollectionListModel>,
    onCollectionItemClick: (collectionId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(FlintTheme.colors.background),
    ) {
        FlintBackTopAppbar(
            onClick = onBackClick,
            title = title,
            backgroundColor = FlintTheme.colors.background,
        )

        Spacer(Modifier.height(12.dp))

        when (collectionList) {
            is UiState.Loading -> {
                FlintLoadingIndicator()
            }

            is UiState.Success -> {
                with(collectionList.data) {
                    Text(
                        text = "총 ${collections.size}개",
                        color = FlintTheme.colors.gray100,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                    )

                    Spacer(Modifier.height(24.dp))

                    LazyVerticalGrid(
                        contentPadding = PaddingValues(10.dp),
                        columns = GridCells.Fixed(2),
                        horizontalArrangement =
                            Arrangement.spacedBy(
                                12.dp,
                                Alignment.CenterHorizontally,
                            ),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier =
                            Modifier
                                .padding(horizontal = 10.dp),
                    ) {
                        items(
                            items = collections,
                            key = { it.id },
                        ) { collection ->
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize(1f)
                                        .align(Alignment.CenterHorizontally),
                            ) {
                                CollectionFileItem(
                                    collection = collection,
                                    modifier =
                                        Modifier
                                            .align(Alignment.Center)
                                            .noRippleClickable(
                                                onClick = { onCollectionItemClick(collection.id) },
                                            ),
                                )
                            }
                        }
                    }
                }

            }

            else -> {}
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CollectionListScreenPreview() {
    FlintTheme {
        CollectionListScreen(
            onBackClick = {},
            title = "전체 컬렉션",
            collectionList = UiState.Success(CollectionListModel.FakeList),
            onCollectionItemClick = {}
        )
    }
}
