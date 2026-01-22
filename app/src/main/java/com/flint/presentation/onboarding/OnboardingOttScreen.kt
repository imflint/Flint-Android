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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val profileUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ottUiState by viewModel.ottUiState.collectAsStateWithLifecycle()

    OnboardingOttScreen(
        nickname = profileUiState.nickname,
        ottUiState = ottUiState,
        onBackClick = navigateUp,
        onNextClick = navigateToDone,
        onOttClick = viewModel::toggleOttSelection,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun OnboardingOttScreen(
    nickname: String,
    ottUiState: OnboardingOttUiState,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onOttClick: (OttType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = FlintTheme.colors.background)
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
            Spacer(modifier = Modifier.height(12.dp))

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
                items(OttType.entries) { ottType ->
                    OnboardingOttItem(
                        ottType = ottType,
                        isSelected = ottUiState.isOttSelected(ottType),
                        onClick = { onOttClick(ottType) },
                    )
                }
            }
        }

        FlintBasicButton(
            text = "다음",
            state = if (ottUiState.canProceed) FlintButtonState.Able else FlintButtonState.Disable,
            onClick = {if (ottUiState.canProceed) onNextClick() },
            contentPadding = PaddingValues(vertical = 13.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),

            )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingOttScreenPreview() {
    FlintTheme {
        OnboardingOttScreen(
            nickname = "차민",
            ottUiState = OnboardingOttUiState(),
            onBackClick = {},
            onNextClick = {},
            onOttClick = {},
        )
    }
}