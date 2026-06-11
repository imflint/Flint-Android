package com.flint.presentation.setting.editprofile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
fun EditProfileRoute(
    navigateUp: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    LaunchedEffect(Unit) {
        viewModel.navigateUp.collect { navigateUp() }
    }

    EditProfileScreen(
        uiState = uiState,
        onBackClick = navigateUp,
        onNicknameChange = viewModel::updateNickname,
        onCheckNickname = viewModel::checkNicknameDuplication,
        onProfileImageSelected = viewModel::updateProfileImage,
        onCompleteClick = viewModel::saveProfile,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileScreen(
    uiState: EditProfileUiState,
    onBackClick: () -> Unit,
    onNicknameChange: (String) -> Unit,
    onCheckNickname: () -> Unit,
    onProfileImageSelected: (Uri?) -> Unit,
    onCompleteClick: () -> Unit,
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

    LaunchedEffect(uiState.hasError, uiState.errorMessage) {
        if (uiState.hasError && uiState.errorMessage != null) {
            toastMessage = uiState.errorMessage ?: ""
            isToastSuccess = false
            showToast = true
        }
    }

    LaunchedEffect(uiState.isNicknameAvailable) {
        if (uiState.isNicknameAvailable == true) {
            toastMessage = "사용 가능한 닉네임입니다"
            isToastSuccess = true
            showToast = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
            .background(FlintTheme.colors.background),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            FlintBackTopAppbar(
                onClick = onBackClick,
                title = "프로필 수정",
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                EditProfileImage(
                    imageUrl = when {
                        uiState.isProfileImageDeleted -> ""
                        uiState.profileImageUri != null -> uiState.profileImageUri.toString()
                        else -> uiState.profileImageUrl ?: ""
                    },
                    onEditClick = { showProfileBottomSheet = true },
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FlintBasicTextField(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        placeholder = "닉네임",
                        value = uiState.nickname,
                        singleLine = true,
                        maxLines = 1,
                        maxLength = EditProfileUiState.MAX_LENGTH,
                        onValueChange = onNicknameChange,
                        borderColor = if (!uiState.isFormatValid || uiState.isNicknameAvailable == false) {
                            FlintTheme.colors.error500
                        } else {
                            Color.Unspecified
                        },
                        trailingContent = {
                            Text(
                                text = "${uiState.nickname.graphemeLength}/${EditProfileUiState.MAX_LENGTH}",
                                style = FlintTheme.typography.body1R16,
                                color = FlintTheme.colors.gray300,
                            )
                        },
                    )

                    Spacer(modifier = Modifier.padding(start = 8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (uiState.canCheckNickname) FlintTheme.colors.primary400
                                else FlintTheme.colors.gray700
                            )
                            .clickable(enabled = uiState.canCheckNickname) {
                                keyboardController?.hide()
                                onCheckNickname()
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "확인",
                            color = if (uiState.canCheckNickname) FlintTheme.colors.white else FlintTheme.colors.gray400,
                            style = if (uiState.canCheckNickname) FlintTheme.typography.body1Sb16 else FlintTheme.typography.body1M16,
                        )
                    }
                }
            }

            FlintLargeButton(
                text = "완료",
                state = if (uiState.canComplete) FlintButtonState.Able else FlintButtonState.Disable,
                onClick = onCompleteClick,
                enabled = uiState.canComplete,
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
private fun EditProfileScreenPreview() {
    FlintTheme {
        EditProfileScreen(
            uiState = EditProfileUiState(
                initialNickname = "한비두비세비",
                nickname = "한비두비세비",
                profileImageUrl = null,
            ),
            onBackClick = {},
            onNicknameChange = {},
            onCheckNickname = {},
            onProfileImageSelected = {},
            onCompleteClick = {},
        )
    }
}
