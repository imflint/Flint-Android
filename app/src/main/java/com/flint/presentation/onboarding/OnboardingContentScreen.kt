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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flint.core.designsystem.component.button.FlintBasicButton
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.image.SelectedContentItem
import com.flint.core.designsystem.component.textfield.FlintSearchTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.component.view.FlintSearchEmptyView
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.onboarding.component.OnboardingContentItem
import com.flint.presentation.onboarding.component.StepProgressBar

@Composable
fun OnboardingContentRoute(
    paddingValues: PaddingValues,
    navigateToOnboardingOtt: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
    ) {

    LaunchedEffect(Unit) {
        viewModel.getSearchContentList(null) // 인기 목록 받아오기
    }

    OnboardingContentScreen(
        nickname = "User",
        currentStep = 7,
        onBackClick = navigateUp,
        onNextClick = navigateToOnboardingOtt,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun OnboardingContentScreen(
    nickname: String,
    currentStep: Int,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    // UI 테스트를 위한 임시 파라미터 (실제 로직 연결 시 ViewModel 상태로 대체)
    isEmptyParams: Boolean = false,
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
            currentStep = currentStep,
            totalSteps = 7,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(23.dp))

        // 전체 콘텐츠를 LazyVerticalGrid로 구성
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            overscrollEffect = null,
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

                    Text(
                        text = "이번 달 가장 재미있었던 작품은?",
                        color = FlintTheme.colors.gray300,
                        style = FlintTheme.typography.body2R14,
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // 검색창 - sticky header (상단에 고정)
            stickyHeader {
                Column(
                    modifier =
                        Modifier
                            .background(FlintTheme.colors.background)
                            .padding(bottom = 16.dp),
                ) {
                    FlintSearchTextField(
                        placeholder = "작품 이름",
                        value = "",
                        onValueChanged = {},
                        onSearchAction = {},
                    )
                }
            }

            // 선택된 영화 가로 스크롤
            item(span = { GridItemSpan(3) }) {
                Column {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        items(7) { index ->
                            SelectedContentItem(
                                imageUrl = "",
                                onRemoveClick = {},
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // 영화 검색 목록 [비어있을 때 || 리스트 있을 때]
            if (isEmptyParams) {
                item(span = { GridItemSpan(3) }) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(300.dp), // 대강 // TODO: 위아래 중앙 배치
                        contentAlignment = Alignment.Center,
                    ) {
                        FlintSearchEmptyView()
                    }
                }
            } else {
                // 영화 목록 그리드
                items(9) { index ->
                    OnboardingContentItem(
                        imageUrl = "",
                        title = "은하수를 여행하는 히치하이커...",
                        director = "가스 제닝스",
                        releaseYear = "2005",
                        isSelected = false,
                        onClick = {},
                        modifier =
                            Modifier.padding(
                                top = if (index >= 3) 20.dp else 0.dp,
                            ),
                    )
                }
            }
        }

        FlintBasicButton(
            text = "다음",
            state = FlintButtonState.Disable,
            onClick = onNextClick,
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
            currentStep = 7,
            onBackClick = {},
            onNextClick = {},
            isEmptyParams = false,
        )
    }
}

@Preview(showBackground = true, name = "검색 결과 없음 상태")
@Composable
private fun OnboardingContentScreenEmptyPreview() {
    FlintTheme {
        OnboardingContentScreen(
            nickname = "안비",
            currentStep = 7,
            onBackClick = {},
            onNextClick = {},
            isEmptyParams = true,
        )
    }
}
