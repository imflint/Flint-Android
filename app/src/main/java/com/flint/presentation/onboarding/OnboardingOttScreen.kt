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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.button.FlintBasicButton
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.type.OttType
import com.flint.presentation.onboarding.component.OnboardingOttItem

@Composable
fun OnboardingOttRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToDone: () -> Unit,
) {
    // 뷰모델
    OnboardingOttScreen(
        nickname = "user",
        onBackClick = navigateUp,
        onNextClick = navigateToDone,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun OnboardingOttScreen(
    nickname: String,
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
            actionText = "건너뛰기",
            textColor = FlintTheme.colors.secondary400,
            onActionClick = onNextClick,
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "$nickname 님이 구독 중인\nOTT 서비스를 알려주세요",
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.display2M28,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(40.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(OttType.getOtts()) { ottType ->
                    OnboardingOttItem(
                        ottType = ottType,
                        isSelected = false, // TODO: 선택 상태 관리 추가
                        onClick = { /* TODO: 선택 토글 로직 추가 */ },
                    )
                }
            }
        }

        FlintBasicButton(
            text = "다음",
            state = FlintButtonState.Disable, // TODO: 선택 상태에 따라 변경
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
private fun OnboardingOttScreenPreview() {
    FlintTheme {
        OnboardingOttScreen(
            nickname = "차민",
            onBackClick = {},
            onNextClick = {},
        )
    }
}
