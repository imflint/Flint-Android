package com.flint.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.core.designsystem.component.button.FlintBasicButton
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.image.ProfileImage
import com.flint.core.designsystem.component.textfield.FlintBasicTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun OnboardingProfileRoute(
    viewModel: OnboardingViewModel,
    paddingValues: PaddingValues,
    navigateToOnboardingContent: () -> Unit,
    navigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardingProfileScreen(
        nickname = uiState.nickname,
        isValid = uiState.isValid,
        onNicknameChange = viewModel::updateNickname,
        onBackClick = navigateUp,
        onNextClick = navigateToOnboardingContent,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun OnboardingProfileScreen(
    nickname: String,
    isValid: Boolean,
    onNicknameChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
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
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileImage(
                imageUrl = "",
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "플린트에서 사용할 이름을 정해주세요.",
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.head3M18,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FlintBasicTextField(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    placeholder = "닉네임",
                    value = nickname,
                    maxLength = OnboardingProfileUiState.MAX_LENGTH,
                    onValueChange = onNicknameChange,
                    trailingContent = {
                        Text(
                            text = "${nickname.length}/${OnboardingProfileUiState.MAX_LENGTH}",
                            style = FlintTheme.typography.body1R16,
                            color = FlintTheme.colors.gray300,
                        )
                    },
                )

                FlintBasicButton(
                    text = "확인",
                    state = if (isValid) FlintButtonState.Able else FlintButtonState.Disable,
                    onClick = onNextClick,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    modifier = Modifier.fillMaxHeight(),
                )
            }
        }

        FlintBasicButton(
            text = "시작하기",
            state = if (isValid) FlintButtonState.Able else FlintButtonState.Disable,
            onClick = onNextClick,
            contentPadding = PaddingValues(12.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingProfileScreenPreview() {
    FlintTheme {
        OnboardingProfileScreen(
            nickname = "안비",
            isValid = true,
            onNicknameChange = {},
            onBackClick = {},
            onNextClick = {},
        )
    }
}