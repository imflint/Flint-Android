package com.flint.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.button.FlintBasicButton
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.image.SelectedFilmItem
import com.flint.core.designsystem.component.textfield.FlintBasicTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.onboarding.component.OnboardingFilmItem
import com.flint.presentation.onboarding.component.StepProgressBar

@Composable
fun OnboardingFilmRoute(
    paddingValues: PaddingValues,
    navigateToOnboardingOtt: () -> Unit,
) {
}

@Composable
fun OnboardingFilmScreen(
    nickname: String,
    currentStep: Int,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = FlintTheme.colors.background)
                .statusBarsPadding(),
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
            // verticalArrangement = Arrangement.spacedBy(20.dp),
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
                    FlintBasicTextField(
                        placeholder = "작품 이름",
                        value = "",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
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
                            SelectedFilmItem(
                                imageUrl = "",
                                onRemoveClick = {},
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // 영화 목록 그리드
            items(9) { index ->
                OnboardingFilmItem(
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

@Preview(showBackground = true)
@Composable
private fun OnboardingFilmScreenPreview() {
    FlintTheme {
        OnboardingFilmScreen(
            nickname = "안비",
            currentStep = 7,
            onBackClick = {},
            onNextClick = {},
        )
    }
}
