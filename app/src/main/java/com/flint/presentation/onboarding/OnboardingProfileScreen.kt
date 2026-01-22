package com.flint.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.R
import com.flint.core.designsystem.component.button.FlintBasicButton
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.image.ProfileImage
import com.flint.core.designsystem.component.textfield.FlintBasicTextField
import com.flint.core.designsystem.component.toast.ShowToast
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun OnboardingProfileRoute(
    paddingValues: PaddingValues,
    navigateToOnboardingContent: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardingProfileScreen(
        nickname = uiState.nickname,
        isValid = uiState.isValid,
        isFormatValid = uiState.isFormatValid,
        isNicknameAvailable = uiState.isNicknameAvailable,
        canProceed = uiState.canProceed,
        hasError = uiState.hasError,
        errorMessage = uiState.errorMessage,
        onNicknameChange = viewModel::updateNickname,
        onCheckNickname = viewModel::checkNicknameDuplication,
        onClearError = viewModel::clearNicknameError,
        onBackClick = navigateUp,
        onNextClick = navigateToOnboardingContent,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun OnboardingProfileScreen(
    nickname: String,
    isValid: Boolean,
    isFormatValid: Boolean,
    isNicknameAvailable: Boolean?,
    canProceed: Boolean,
    hasError: Boolean,
    errorMessage: String?,
    onNicknameChange: (String) -> Unit,
    onCheckNickname: () -> Unit,
    onClearError: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var isToastSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(hasError, errorMessage) {
        if (hasError && errorMessage != null) {
            toastMessage = errorMessage
            isToastSuccess = false
            showToast = true
        }
    }

    LaunchedEffect(isNicknameAvailable) {
        if (isNicknameAvailable == true) {
            toastMessage = "사용 가능한 닉네임입니다"
            isToastSuccess = true
            showToast = true
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = FlintTheme.colors.background)
        ) {
            FlintBackTopAppbar(
                onClick = onBackClick,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ProfileImage(
                    imageUrl = "",
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "어떤 이름으로 불러드릴까요?",
                    color = FlintTheme.colors.white,
                    style = FlintTheme.typography.head3M18,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FlintBasicTextField(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        placeholder = "닉네임",
                        value = nickname,
                        maxLines = 1,
                        maxLength = OnboardingProfileUiState.MAX_LENGTH,
                        onValueChange = onNicknameChange,
                        borderColor = if (hasError) FlintTheme.colors.error500 else Color.Unspecified,
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
                        state = if (isValid && isFormatValid) FlintButtonState.Able else FlintButtonState.Disable,
                        onClick = {
                            keyboardController?.hide()
                            onCheckNickname()
                        },
                        enabled = isValid && isFormatValid,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        modifier = Modifier.fillMaxHeight(),
                    )
                }
            }

            FlintBasicButton(
                text = "시작하기",
                state = if (canProceed) FlintButtonState.Able else FlintButtonState.Disable,
                onClick = onNextClick,
                enabled = canProceed,
                contentPadding = PaddingValues(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
            )
        }

        if (showToast) {
            ShowToast(
                text = toastMessage,
                imageVector = ImageVector.vectorResource(
                    if (isToastSuccess) R.drawable.ic_check else R.drawable.ic_x
                ),
                paddingValues = PaddingValues.Zero,
                yOffset = 100.dp,
                hide = {
                    showToast = false
                    if (!isToastSuccess) {
                        onClearError()
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingProfileScreenPreview() {
    FlintTheme {
        OnboardingProfileScreen(
            nickname = "안비",
            isValid = true,
            isFormatValid = true,
            isNicknameAvailable = true,
            canProceed = true,
            hasError = false,
            errorMessage = null,
            onNicknameChange = {},
            onCheckNickname = {},
            onClearError = {},
            onBackClick = {},
            onNextClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingProfileScreenDuplicateErrorPreview() {
    FlintTheme {
        OnboardingProfileScreen(
            nickname = "안비",
            isValid = true,
            isFormatValid = true,
            isNicknameAvailable = false,
            canProceed = false,
            hasError = true,
            errorMessage = "이미 사용 중인 닉네임입니다",
            onNicknameChange = {},
            onCheckNickname = {},
            onClearError = {},
            onBackClick = {},
            onNextClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingProfileScreenFormatErrorPreview() {
    FlintTheme {
        OnboardingProfileScreen(
            nickname = "안비123",
            isValid = true,
            isFormatValid = false,
            isNicknameAvailable = null,
            canProceed = false,
            hasError = true,
            errorMessage = "사용할 수 없는 닉네임입니다",
            onNicknameChange = {},
            onCheckNickname = {},
            onClearError = {},
            onBackClick = {},
            onNextClick = {},
        )
    }
}
