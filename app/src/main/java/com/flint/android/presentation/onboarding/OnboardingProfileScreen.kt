package com.flint.android.presentation.onboarding

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chattymin.pebble.graphemeLength
import com.flint.android.R
import com.flint.android.core.designsystem.component.bottomsheet.MenuBottomSheet
import com.flint.android.core.designsystem.component.bottomsheet.MenuBottomSheetData
import com.flint.android.core.designsystem.component.button.FlintButtonState
import com.flint.android.core.designsystem.component.button.FlintLargeButton
import com.flint.android.core.designsystem.component.image.EditProfileImage
import com.flint.android.core.designsystem.component.textfield.FlintBasicTextField
import com.flint.android.core.designsystem.component.toast.ShowToast
import com.flint.android.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.android.core.designsystem.interaction.pressClickable
import com.flint.android.core.designsystem.interaction.pressScale
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.presentation.onboarding.event.OnboardingProfileEvent
import kotlinx.coroutines.delay

@Composable
fun OnboardingProfileRoute(
    paddingValues: PaddingValues,
    navigateToOnboardingDone: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var isToastSuccess by remember { mutableStateOf(false) }

    // 닉네임 입력 페이지에 (재)진입할 때마다 실행된다.
    // End 화면까지 갔다가 시작하기를 누르지 않고 뒤로 돌아온 경우를 포함해,
    // 이전에 입력했던 닉네임이 남아있어도 중복확인 결과는 항상 다시 확인하도록 초기화한다.
    LaunchedEffect(Unit) {
        viewModel.resetNicknameCheck()
    }

    LaunchedEffect(Unit) {
        viewModel.profileEvent.collect { event ->
            when (event) {
                is OnboardingProfileEvent.ShowNicknameToast -> {
                    toastMessage = event.message
                    isToastSuccess = event.isSuccess
                    showToast = true
                }
            }
        }
    }

    OnboardingProfileScreen(
        nickname = uiState.nickname,
        isValid = uiState.isValid,
        isFormatValid = uiState.isFormatValid,
        isNicknameAvailable = uiState.isNicknameAvailable,
        canProceed = uiState.canProceed,
        canCheckNickname = uiState.canCheckNickname,
        profileImageUri = uiState.profileImageUri,
        showToast = showToast,
        toastMessage = toastMessage,
        isToastSuccess = isToastSuccess,
        onToastHide = { showToast = false },
        onNicknameChange = viewModel::updateNickname,
        onCheckNickname = viewModel::checkNicknameDuplication,
        onProfileImageSelected = viewModel::updateProfileImage,
        onBackClick = navigateUp,
        onNextClick = navigateToOnboardingDone,
        modifier = Modifier
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues),
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OnboardingProfileScreen(
    nickname: String,
    isValid: Boolean,
    isFormatValid: Boolean,
    isNicknameAvailable: Boolean?,
    canProceed: Boolean,
    canCheckNickname: Boolean,
    profileImageUri: Uri?,
    showToast: Boolean,
    toastMessage: String,
    isToastSuccess: Boolean,
    onToastHide: () -> Unit,
    onNicknameChange: (String) -> Unit,
    onCheckNickname: () -> Unit,
    onProfileImageSelected: (Uri?) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showProfileBottomSheet by rememberSaveable { mutableStateOf(false) }
    // 0이면 대기 중인 요청이 없다는 뜻. 클릭할 때마다 증가시켜서, 이전 요청과 새 요청을
    // 구분하고(회전 등 config change에도 rememberSaveable로 값이 유지된다).
    var pendingBottomSheetToken by rememberSaveable { mutableStateOf(0) }
    val imeVisible = WindowInsets.isImeVisible

    LaunchedEffect(pendingBottomSheetToken, imeVisible) {
        val token = pendingBottomSheetToken
        if (token == 0) return@LaunchedEffect

        if (imeVisible) {
            // 키보드가 내려갔다는 신호(WindowInsets.isImeVisible == false)가 오지 않는
            // 기기(하드웨어 키보드 등)를 대비해, 일정 시간 뒤에는 강제로 바텀시트를 띄운다.
            delay(300)
        }

        // delay 도중 새 클릭이 들어와 token이 바뀌었다면 그 요청은 무시한다.
        if (pendingBottomSheetToken == token) {
            pendingBottomSheetToken = 0
            showProfileBottomSheet = true
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) onProfileImageSelected(uri)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = FlintTheme.colors.background)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
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
                        onEditClick = {
                            // 키보드가 떠 있으면 먼저 내리고, 완전히 내려간 뒤(위 LaunchedEffect)에
                            // 바텀시트를 띄운다. 키보드가 이미 없으면 바로 띄운다.
                            if (imeVisible) {
                                keyboardController?.hide()
                                pendingBottomSheetToken += 1
                            } else {
                                showProfileBottomSheet = true
                            }
                        }
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

                        val checkInteractionSource = remember { MutableInteractionSource() }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .pressScale(checkInteractionSource, enabled = canCheckNickname)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (canCheckNickname) FlintTheme.colors.primary400
                                    else FlintTheme.colors.gray700
                                )
                                .pressClickable(
                                    interactionSource = checkInteractionSource,
                                    enabled = canCheckNickname,
                                    role = Role.Button,
                                ) {
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
                hide = onToastHide,
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
            profileImageUri = null,
            showToast = false,
            toastMessage = "",
            isToastSuccess = false,
            onToastHide = {},
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
            profileImageUri = null,
            showToast = true,
            toastMessage = "이미 사용 중인 닉네임입니다",
            isToastSuccess = false,
            onToastHide = {},
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
            canCheckNickname = true,
            profileImageUri = null,
            showToast = true,
            toastMessage = "사용할 수 없는 닉네임입니다",
            isToastSuccess = false,
            onToastHide = {},
            onNicknameChange = { text = it },
            onCheckNickname = {},
            onProfileImageSelected = {},
            onBackClick = {},
            onNextClick = {},
        )
    }
}
