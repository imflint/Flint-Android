package com.flint.presentation.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.R
import com.flint.core.common.util.UiState
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintLargeButton
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.component.indicator.FlintLoadingIndicator
import com.flint.core.designsystem.component.topappbar.FlintLogoTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.collection.CollectionsModel
import com.flint.presentation.explore.uistate.ExploreUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ExploreRoute(
    paddingValues: PaddingValues,
    navigateToCollectionDetail: (collectionId: String, imageUrl: String) -> Unit,
    navigateToCollectionCreate: () -> Unit,
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    val uiState: UiState<ExploreUiState> by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        UiState.Loading -> {
            FlintLoadingIndicator()
        }

        is UiState.Success -> {
            val uiState = (uiState as UiState.Success<ExploreUiState>).data
            ExploreScreen(
                collections = uiState.collections,
                onWatchCollectionButtonClick = navigateToCollectionDetail,
                onMakeCollectionButtonClick = navigateToCollectionCreate,
                onLoadNextPage = viewModel::loadNextPage,
                modifier = Modifier.padding(paddingValues),
            )
        }

        else -> {}
    }
}

@Composable
private fun ExploreScreen(
    collections: ImmutableList<CollectionsModel.Collection>,
    onWatchCollectionButtonClick: (collectionId: String, imageUrl: String) -> Unit,
    onMakeCollectionButtonClick: () -> Unit,
    onLoadNextPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pageCount: Int = collections.size
    val pagerState: PagerState = rememberPagerState(pageCount = { pageCount + 1 })

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage >= pageCount - 3 && pageCount > 0) {
            onLoadNextPage()
        }
    }

    Column(
        modifier
            .run {
                if (pagerState.currentPage < pageCount) {
                    background(FlintTheme.colors.background)
                } else {
                    background(
                        FlintTheme.colors.gradient900,
                    )
                }
            }
            .fillMaxSize(),
    ) {
        FlintLogoTopAppbar(backgroundColor = Color.Transparent)

        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page: Int ->
            val collection: CollectionsModel.Collection? = collections.getOrNull(page)

            if (collection != null) {
                ExplorePageItem(
                    imageUrl = collection.imageUrl,
                    id = collection.collectionId,
                    contentTitle = collection.contentTitle,
                    contentDescription = collection.contentDescription,
                    onButtonClick = { onWatchCollectionButtonClick(it, collection.imageUrl) },
                )
            } else {
                ExploreEndPage(
                    onButtonClick = onMakeCollectionButtonClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun ExplorePageItem(
    imageUrl: String,
    id: String,
    contentTitle: String,
    contentDescription: String,
    onButtonClick: (collectionId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(FlintTheme.colors.background)
                .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        NetworkImage(
            imageUrl = imageUrl,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier =
                Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF121212).copy(0f),
                                Color(0xFF121212).copy(1f)
                            ),
                        )
                    )
                    .fillMaxSize(),
        )

        Column(modifier.padding(horizontal = 16.dp)) {
            Text(
                text = contentTitle,
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.display2M28,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = contentDescription,
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.body1R16,
                maxLines = 8,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(28.dp))

            FlintLargeButton(
                text = "이 컬렉션 보러가기",
                state = FlintButtonState.ColorOutline,
                onClick = { onButtonClick(id) },
                modifier =
                    Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp),
            )
        }
    }
}

@Preview
@Composable
private fun ExplorePageItemPreview() {
    FlintTheme {
        ExplorePageItem(
            imageUrl = "https://buly.kr/G3Edbfu",
            id = "",
            contentTitle = "너의 모든 것".repeat(10),
            contentDescription =
                """
                뉴욕의 서점 매니저이자 반듯한 독서가, 조. 
                그가 대학원생 벡을 만나 한눈에 반한다. 
                하지만 훈훈했던 그의 첫인상은 잠시일 뿐, 
                감추어진 조의 뒤틀린 이면이 드러난다.
                
                """.trimIndent().repeat(10),
            onButtonClick = {},
        )
    }
}

@Composable
private fun ExploreEndPage(
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .background(FlintTheme.colors.gradient900)
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.ic_gradient_pencil),
            contentDescription = null,
        )

        Spacer(Modifier.height(47.dp))

        Text(
            text = "지금 뜨는 추천을 모두 살펴봤어요",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.head1Sb22,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "마음에 남는 작품들로\n나만의 컬렉션을 만들어보세요!",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.body1M16,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.weight(1f))

        FlintLargeButton(
            text = "컬렉션 만들러 가기",
            state = FlintButtonState.Able,
            onClick = onButtonClick,
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun ExploreEndPagePreview() {
    FlintTheme {
        ExploreEndPage(
            {},
        )
    }
}

@Preview
@Composable
private fun ExploreScreenPreview() {
    FlintTheme {
        ExploreScreen(
            collections =
                List(10) {
                    CollectionsModel.Collection(
                        collectionId = "0",
                        contentTitle = "너의 모든 것",
                        imageUrl = "https://buly.kr/G3Edbfu",
                        contentDescription =
                            """
                            뉴욕의 서점 매니저이자 반듯한 독서가, 조.
                            그가 대학원생 벡을 만나 한눈에 반한다.
                            하지만 훈훈했던 그의 첫인상은 잠시일 뿐,
                            감추어진 조의 뒤틀린 이면이 드러난다.
                            """.trimIndent(),
                    )
                }.toImmutableList(),
            onWatchCollectionButtonClick = { _, _ -> },
            onMakeCollectionButtonClick = {},
            onLoadNextPage = {},
            modifier =
                Modifier
                    .padding(PaddingValues())
                    .systemBarsPadding()
                    .navigationBarsPadding(),
        )
    }
}
