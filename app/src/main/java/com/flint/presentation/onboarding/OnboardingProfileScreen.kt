package com.flint.presentation.onboarding

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chattymin.pebble.graphemeLength
import com.flint.R
import com.flint.core.designsystem.component.bottomsheet.MenuBottomSheet
import com.flint.core.designsystem.component.bottomsheet.MenuBottomSheetData
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintLargeButton
import com.flint.core.designsystem.component.image.EditProfileImage
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
        canCheckNickname = uiState.canCheckNickname,
        hasError = uiState.hasError,
        errorMessage = uiState.errorMessage,
        profileImageUri = uiState.profileImageUri,
        onNicknameChange = viewModel::updateNickname,
        onCheckNickname = viewModel::checkNicknameDuplication,
        onProfileImageSelected = viewModel::updateProfileImage,
        onBackClick = navigateUp,
        onNextClick = navigateToOnboardingContent,
        modifier = Modifier
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingProfileScreen(
    nickname: String,
    isValid: Boolean,
    isFormatValid: Boolean,
    isNicknameAvailable: Boolean?,
    canProceed: Boolean,
    canCheckNickname: Boolean,
    hasError: Boolean,
    errorMessage: String?,
    profileImageUri: Uri?,
    onNicknameChange: (String) -> Unit,
    onCheckNickname: () -> Unit,
    onProfileImageSelected: (Uri?) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var isToastSuccess by remember { mutableStateOf(false) }
    var showProfileBottomSheet by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) onProfileImageSelected(uri)
    }

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
                .imePadding()
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

                EditProfileImage(
                    imageUrl = profileImageUri?.toString() ?: "",
                    onEditClick = { showProfileBottomSheet = true }
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
                        singleLine = true,
                        maxLines = 1,
                        maxLength = OnboardingProfileUiState.MAX_LENGTH,
                        onValueChange = onNicknameChange,
                        borderColor = if (!isFormatValid || isNicknameAvailable == false) {
                            FlintTheme.colors.error500
                        } else {
                            Color.Unspecified
                        },
                        trailingContent = {
                            Text(
                                text = "${nickname.graphemeLength}/${OnboardingProfileUiState.MAX_LENGTH}",
                                style = FlintTheme.typography.body1R16,
                                color = FlintTheme.colors.gray300,
                            )
                        },
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (canCheckNickname) FlintTheme.colors.primary400
                                else FlintTheme.colors.gray700
                            )
                            .clickable(enabled = canCheckNickname) {
                                keyboardController?.hide()
                                onCheckNickname()
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "확인",
                            color = if (canCheckNickname) FlintTheme.colors.white else FlintTheme.colors.gray400,
                            style = if (canCheckNickname) FlintTheme.typography.body1Sb16 else FlintTheme.typography.body1M16,
                        )
                    }
                }
            }

            FlintLargeButton(
                text = "다음",
                state = if (canProceed) FlintButtonState.Able else FlintButtonState.Disable,
                onClick = onNextClick,
                enabled = canProceed,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
            )
        }

        if (showProfileBottomSheet) {
            MenuBottomSheet(
                menuBottomSheetDataList = listOf(
                    MenuBottomSheetData(
                        label = "갤러리에서 선택",
                        clickAction = {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ),
                    MenuBottomSheetData(
                        label = "프로필 사진 삭제",
                        color = FlintTheme.colors.error500,
                        clickAction = { onProfileImageSelected(null) }
                    ),
                ),
                onDismiss = { showProfileBottomSheet = false }
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
                hide = { showToast = false },
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
            canCheckNickname = true,
            hasError = false,
            errorMessage = null,
            profileImageUri = null,
            onNicknameChange = {},
            onCheckNickname = {},
            onProfileImageSelected = {},
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
            canCheckNickname = true,
            hasError = true,
            errorMessage = "이미 사용 중인 닉네임입니다",
            profileImageUri = null,
            onNicknameChange = {},
            onCheckNickname = {},
            onProfileImageSelected = {},
            onBackClick = {},
            onNextClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingProfileScreenFormatErrorPreview() {
    var text by remember { mutableStateOf("") }

    FlintTheme {
        OnboardingProfileScreen(
            nickname = text,
            isValid = true,
            isFormatValid = false,
            isNicknameAvailable = null,
            canProceed = false,
            canCheckNickname = false,
            hasError = true,
            errorMessage = "사용할 수 없는 닉네임입니다",
            profileImageUri = null,
            onNicknameChange = { text = it },
            onCheckNickname = {},
            onProfileImageSelected = {},
            onBackClick = {},
            onNextClick = {},
        )
    }
}
