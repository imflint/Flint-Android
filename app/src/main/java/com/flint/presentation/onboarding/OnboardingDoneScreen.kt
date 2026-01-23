package com.flint.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.R
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintLargeButton
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun OnboardingDoneRoute(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val signupUiState by viewModel.signupUiState.collectAsStateWithLifecycle()

    // 회원가입 성공 시 Home으로 이동
    LaunchedEffect(signupUiState.isSuccess) {
        if (signupUiState.isSuccess) {
            navigateToHome()
        }
    }

    OnboardingDoneScreen(
        isLoading = signupUiState.isLoading,
        onBackClick = navigateUp,
        onStartClick = viewModel::signup,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun OnboardingDoneScreen(
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onStartClick: () -> Unit,
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
        )

        Column(
            modifier =
                Modifier
                    .weight(1f),
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "취향이 보이기 시작했어요",
                color = FlintTheme.colors.primary200,
                style = FlintTheme.typography.body1R16,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Flint에서 끌리는 콘텐츠를\n만나러 가볼까요?",
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.display2M28,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.img_onboarding_3d),
                contentDescription = null,
                modifier =
                    Modifier
                        .fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
        }

        FlintLargeButton(
            text = "시작하기",
            state = if (isLoading) FlintButtonState.Disable else FlintButtonState.Able,
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),

            )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingDoneScreenPreview() {
    FlintTheme {
        OnboardingDoneScreen(
            isLoading = false,
            onBackClick = {},
            onStartClick = {},
        )
    }
}