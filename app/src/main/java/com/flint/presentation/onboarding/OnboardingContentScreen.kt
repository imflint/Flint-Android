package com.flint.presentation.onboarding

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.core.common.extension.dropShadow
import com.flint.core.common.util.UiState
import com.flint.core.designsystem.component.button.FlintBasicButton
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.image.SelectedContentItem
import com.flint.core.designsystem.component.textfield.FlintSearchTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.component.view.FlintSearchEmptyView
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.search.SearchContentItemModel
import com.flint.domain.model.search.SearchContentListModel
import com.flint.presentation.onboarding.component.OnboardingContentItem
import com.flint.presentation.onboarding.component.StepProgressBar

@Composable
fun OnboardingContentRoute(
    paddingValues: PaddingValues,
    navigateToOnboardingOtt: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val profileUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val contentUiState by viewModel.contentUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadInitialContents()
    }

    OnboardingContentScreen(
        nickname = profileUiState.nickname,
        contentUiState = contentUiState,
        onBackClick = navigateUp,
        onNextClick = navigateToOnboardingOtt,
        onSearchKeywordChanged = viewModel::updateSearchKeyword,
        onSearchAction = viewModel::searchContents,
        onContentClick = viewModel::toggleContentSelection,
        onRemoveContent = viewModel::toggleContentSelection,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun OnboardingContentScreen(
    nickname: String,
    contentUiState: OnboardingContentUiState,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onSearchKeywordChanged: (String) -> Unit,
    onSearchAction: () -> Unit,
    onContentClick: (SearchContentItemModel) -> Unit,
    onRemoveContent: (SearchContentItemModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = FlintTheme.colors.background),
    ) {
        FlintBackTopAppbar(
            onClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        StepProgressBar(
            currentStep = contentUiState.selectedContents.size,
            totalSteps = OnboardingContentUiState.REQUIRED_SELECTION_COUNT,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(23.dp))

        // 전체 콘텐츠를 LazyVerticalGrid로 구성
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // 타이틀 영역 - 스크롤됨
            item(span = { GridItemSpan(3) }) {
                Column {
                    Text(
                        text = "${nickname}님이 좋아하는 작품\n7개를 골라주세요",
                        color = FlintTheme.colors.white,
                        style = FlintTheme.typography.display2M28,
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // 검색창 - sticky header (상단에 고정)
            stickyHeader {
                Column(
                    modifier =
                        Modifier
                            .background(FlintTheme.colors.background)
                            .padding(bottom = 16.dp)
                            .dropShadow(
                                shape = RectangleShape,
                                color = Color.Black.copy(alpha = 0.75f),
                                blur = 50.dp,
                                offsetY = 20.dp,
                                offsetX = 0.dp,
                                spread = 0.dp
                            )
                ) {
                    Text(
                        text = contentUiState.currentStepQuestion,
                        color = FlintTheme.colors.gray300,
                        style = FlintTheme.typography.body2R14,
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    FlintSearchTextField(
                        placeholder = "작품 이름",
                        value = contentUiState.searchKeyword,
                        onValueChanged = onSearchKeywordChanged,
                        onSearchAction = onSearchAction,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = { onSearchAction() }
                        ),
                    )

                    // 선택된 영화 가로 스크롤
                    if (contentUiState.selectedContents.isNotEmpty()) {
                        val lazyListState = rememberLazyListState()

                        // 새로운 아이템이 추가될 때 왼쪽 자동 스크롤
                        LaunchedEffect(contentUiState.selectedContents.size) {
                            lazyListState.animateScrollToItem(0)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        LazyRow(
                            state = lazyListState,
                            horizontalArrangement = Arrangement.spacedBy(0.dp),
                        ) {
                            items(
                                items = contentUiState.selectedContents,
                                key = { it.id }
                            ) { content ->
                                SelectedContentItem(
                                    imageUrl = content.posterUrl,
                                    onRemoveClick = { onRemoveContent(content) },
                                )
                            }
                        }
                    }
                }
            }

            // 영화 검색 목록
            when (val searchResultsState = contentUiState.searchResults) {
                is UiState.Empty, is UiState.Failure -> {
                    item(span = { GridItemSpan(3) }) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            FlintSearchEmptyView(
                                title = "작품을 찾을 수 없어요"
                            )
                        }
                    }
                }
                is UiState.Loading -> {
                    // 로딩
                }
                is UiState.Success -> {
                    // 영화 목록 그리드
                    itemsIndexed(
                        items = searchResultsState.data,
                        key = { _, content -> content.id }
                    ) { index, content ->
                        val topPadding = if (index >= 3) 20.dp else 0.dp

                        OnboardingContentItem(
                            imageUrl = content.posterUrl,
                            title = content.title,
                            director = content.author,
                            releaseYear = content.year.toString(),
                            isSelected = contentUiState.isContentSelected(content.id),
                            onClick = { onContentClick(content) },
                            modifier = Modifier.padding(top = topPadding),
                        )
                    }
                }
            }
        }

        FlintBasicButton(
            text = "다음",
            state = if (contentUiState.canProceed) FlintButtonState.Able else FlintButtonState.Disable,
            onClick = { if (contentUiState.canProceed) { onNextClick() } },
            contentPadding = PaddingValues(vertical = 14.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
        )
    }
}

@Preview(showBackground = true, name = "기본 목록 상태")
@Composable
private fun OnboardingContentScreenListPreview() {
    FlintTheme {
        OnboardingContentScreen(
            nickname = "안비",
            contentUiState = OnboardingContentUiState(
                searchResults = UiState.Success(
                    SearchContentListModel.FakeList
                ),
            ),
            onBackClick = {},
            onNextClick = {},
            onSearchKeywordChanged = {},
            onSearchAction = {},
            onContentClick = {},
            onRemoveContent = {},
        )
    }
}

@Preview(showBackground = true, name = "검색 결과 없음 상태")
@Composable
private fun OnboardingContentScreenEmptyPreview() {
    FlintTheme {
        OnboardingContentScreen(
            nickname = "안비",
            contentUiState = OnboardingContentUiState(),
            onBackClick = {},
            onNextClick = {},
            onSearchKeywordChanged = {},
            onSearchAction = {},
            onContentClick = {},
            onRemoveContent = {},
        )
    }
}