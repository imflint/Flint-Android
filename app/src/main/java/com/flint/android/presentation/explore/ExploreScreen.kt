package com.flint.android.presentation.explore

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
import com.flint.android.R
import com.flint.android.core.common.util.UiState
import com.flint.android.core.designsystem.component.button.FlintButtonState
import com.flint.android.core.designsystem.component.button.FlintLargeButton
import com.flint.android.core.designsystem.component.image.NetworkImage
import com.flint.android.core.designsystem.component.indicator.FlintLoadingIndicator
import com.flint.android.core.designsystem.component.topappbar.FlintLogoTopAppbar
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.model.exploration.ExplorationItemModel
import com.flint.android.presentation.explore.uistate.ExploreUiState
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
            val data = (uiState as UiState.Success<ExploreUiState>).data

            if (data.isEmpty) {
                ExploreEmptyPage(modifier = Modifier.padding(paddingValues))
            } else {
                ExploreScreen(
                    items = data.items,
                    isEnd = data.isEnd,
                    initialPage = data.initialPage,
                    onWatchCollectionButtonClick = navigateToCollectionDetail,
                    onMakeCollectionButtonClick = navigateToCollectionCreate,
                    onLoadNextSession = viewModel::advanceToNextSession,
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }

        UiState.Failure -> {
            ExploreErrorPage(
                onRetryClick = viewModel::retry,
                modifier = Modifier.padding(paddingValues),
            )
        }

        else -> {}
    }
}

@Composable
private fun ExploreScreen(
    items: ImmutableList<ExplorationItemModel>,
    isEnd: Boolean,
    initialPage: Int,
    onWatchCollectionButtonClick: (collectionId: String, imageUrl: String) -> Unit,
    onMakeCollectionButtonClick: () -> Unit,
    onLoadNextSession: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemCount: Int = items.size
    val pagerState: PagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { itemCount + 1 },
    )

    LaunchedEffect(pagerState.currentPage) {
        if (!isEnd && pagerState.currentPage >= itemCount - 3 && itemCount > 0) {
            onLoadNextSession()
        }
    }

    Column(
        modifier
            .run {
                if (pagerState.currentPage < itemCount) {
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
            val item: ExplorationItemModel? = items.getOrNull(page)

            when {
                item != null -> {
                    ExplorePageItem(
                        imageUrl = item.imageUrl,
                        collectionId = item.collectionId,
                        contentTitle = item.title,
                        contentDescription = item.description,
                        year = item.year,
                        onButtonClick = { onWatchCollectionButtonClick(it, item.imageUrl) },
                    )
                }
                // isEnd == true일 때만 진짜 End 화면을 보여준다.
                isEnd -> {
                    ExploreEndPage(
                        onButtonClick = onMakeCollectionButtonClick,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                // isEnd == false인데 item이 없는 경우(다음 세션 로딩 중/실패 등)는
                // End로 오해하지 않도록 로딩 표시만 하고 대기한다.
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(FlintTheme.colors.background),
                        contentAlignment = Alignment.Center,
                    ) {
                        FlintLoadingIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun ExplorePageItem(
    imageUrl: String,
    collectionId: String,
    contentTitle: String,
    contentDescription: String,
    year: Int,
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
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(28.dp))

            FlintLargeButton(
                text = "이 컬렉션 보러가기",
                state = FlintButtonState.ColorOutline,
                onClick = { onButtonClick(collectionId) },
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
            collectionId = "",
            contentTitle = "너의 모든 것".repeat(10),
            contentDescription =
                """
                뉴욕의 서점 매니저이자 반듯한 독서가, 조.
                그가 대학원생 벡을 만나 한눈에 반한다.
                하지만 훈훈했던 그의 첫인상은 잠시일 뿐,
                감추어진 조의 뒤틀린 이면이 드러난다.

                """.trimIndent().repeat(10),
            year = 2014,
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

// 아직 첫 탐색 세션(30개)이 준비되지 않은 경우(state == EMPTY)
@Composable
private fun ExploreEmptyPage(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .background(FlintTheme.colors.background)
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FlintLogoTopAppbar(backgroundColor = Color.Transparent)

        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.ic_gradient_pencil),
            contentDescription = null,
        )

        Spacer(Modifier.height(47.dp))

        Text(
            text = "탐색 콘텐츠를 준비하고 있어요",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.head1Sb22,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "곧 새로운 작품들로 찾아올게요\n조금만 기다려주세요!",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.body1M16,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun ExploreEmptyPagePreview() {
    FlintTheme {
        ExploreEmptyPage()
    }
}

// 탐색 세션 조회(getExplorationSession) 실패 시(state == Failure)
@Composable
private fun ExploreErrorPage(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .background(FlintTheme.colors.background)
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FlintLogoTopAppbar(backgroundColor = Color.Transparent)

        Spacer(Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.ic_gradient_pencil),
            contentDescription = null,
        )

        Spacer(Modifier.height(47.dp))

        Text(
            text = "탐색 콘텐츠를 불러오지 못했어요",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.head1Sb22,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "네트워크 상태를 확인한 뒤\n다시 시도해주세요!",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.body1M16,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.weight(1f))

        FlintLargeButton(
            text = "다시 시도하기",
            state = FlintButtonState.Able,
            onClick = onRetryClick,
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
private fun ExploreErrorPagePreview() {
    FlintTheme {
        ExploreErrorPage(onRetryClick = {})
    }
}

@Preview
@Composable
private fun ExploreScreenPreview() {
    FlintTheme {
        ExploreScreen(
            items =
                List(10) {
                    ExplorationItemModel(
                        contentId = "0",
                        title = "너의 모든 것",
                        description =
                            """
                            뉴욕의 서점 매니저이자 반듯한 독서가, 조.
                            그가 대학원생 벡을 만나 한눈에 반한다.
                            하지만 훈훈했던 그의 첫인상은 잠시일 뿐,
                            감추어진 조의 뒤틀린 이면이 드러난다.
                            """.trimIndent(),
                        imageUrl = "https://buly.kr/G3Edbfu",
                        year = 2014,
                        collectionId = "0",
                    )
                }.toImmutableList(),
            isEnd = false,
            initialPage = 0,
            onWatchCollectionButtonClick = { _, _ -> },
            onMakeCollectionButtonClick = {},
            onLoadNextSession = {},
            modifier =
                Modifier
                    .padding(PaddingValues())
                    .systemBarsPadding()
                    .navigationBarsPadding(),
        )
    }
}
