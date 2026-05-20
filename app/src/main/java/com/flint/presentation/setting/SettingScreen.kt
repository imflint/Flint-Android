package com.flint.presentation.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintMediumButton
import com.flint.core.designsystem.component.image.ProfileImage
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun SettingRoute(
    navigateUp: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToEditProfile: () -> Unit,
    navigateToWithdraw: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadUserInfo()
    }

    LaunchedEffect(Unit) {
        viewModel.navigateToLogin.collect {
            navigateToLogin()
        }
    }

    SettingScreen(
        uiState = uiState,
        onBackClick = navigateUp,
        onEditProfileClick = navigateToEditProfile,
        onLogoutClick = viewModel::showLogoutDialog,
        onWithdrawClick = navigateToWithdraw,
        onLogoutConfirm = {
            viewModel.dismissLogoutDialog()
            viewModel.logout()
        },
        onLogoutDismiss = viewModel::dismissLogoutDialog,
    )
}

@Composable
private fun SettingScreen(
    uiState: SettingUiState,
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onLogoutDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FlintTheme.colors.background),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            FlintBackTopAppbar(
                onClick = onBackClick,
                title = "설정",
            )

            SettingProfileSection(
                nickname = uiState.nickname,
                profileImageUrl = uiState.profileImageUrl,
                onEditProfileClick = onEditProfileClick,
                modifier = Modifier.fillMaxWidth(),
            )

            HorizontalDivider(color = FlintTheme.colors.gray700, thickness = 12.dp)

            SettingMenuItem(
                label = "계정",
                trailingContent = {
                    if (uiState.email.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = uiState.email,
                                style = FlintTheme.typography.body1M16,
                                color = FlintTheme.colors.white,
                            )
                            Spacer(Modifier.width(4.dp))
                            Image(
                                painter = painterResource(R.drawable.ic_kakao_full),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                },
            )

            HorizontalDivider(color = FlintTheme.colors.gray700, thickness = 1.dp)

            SettingMenuItem(label = "개인정보 정책")

            HorizontalDivider(color = FlintTheme.colors.gray700, thickness = 1.dp)

            SettingMenuItem(label = "이용약관")

            HorizontalDivider(color = FlintTheme.colors.gray700, thickness = 1.dp)

            SettingMenuItem(
                label = "로그아웃",
                onClick = onLogoutClick,
            )

            HorizontalDivider(color = FlintTheme.colors.gray700, thickness = 1.dp)

            Spacer(Modifier.weight(1f))

            Text(
                text = "탈퇴하기",
                style = FlintTheme.typography.body2M14,
                color = FlintTheme.colors.gray300,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .noRippleClickable(onClick = onWithdrawClick)
                    .padding(vertical = 16.dp),
            )
        }
    }

    if (uiState.isLogoutDialogVisible) {
        SettingConfirmDialog(
            title = "로그아웃",
            message = "로그아웃 하시겠어요?",
            confirmText = "로그아웃",
            onConfirm = onLogoutConfirm,
            onDismiss = onLogoutDismiss,
        )
    }

}

@Composable
private fun SettingProfileSection(
    nickname: String,
    profileImageUrl: String?,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(FlintTheme.colors.background)
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 28.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileImage(
            imageUrl = profileImageUrl,
            modifier = Modifier.size(56.dp),
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = nickname,
            style = FlintTheme.typography.body1M16,
            color = FlintTheme.colors.white,
            modifier = Modifier.weight(1f),
        )

        Spacer(Modifier.width(12.dp))

        FlintMediumButton(
            text = "프로필 수정",
            state = FlintButtonState.ColorOutline,
            onClick = onEditProfileClick,
            textStyle = FlintTheme.typography.body2M14,
            modifier = Modifier.weight(0.8f),
        )
    }
}

@Composable
private fun SettingMenuItem(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = if(label == "이용약관") 25.dp else 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = FlintTheme.typography.body1M16,
            color = FlintTheme.colors.white,
            modifier = Modifier.weight(1f),
        )
        trailingContent()
    }
}

@Composable
private fun SettingConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmColor: Color = FlintTheme.colors.error500,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = FlintTheme.colors.gray800,
        title = {
            Text(
                text = title,
                style = FlintTheme.typography.head3Sb18,
                color = FlintTheme.colors.white,
            )
        },
        text = {
            Text(
                text = message,
                style = FlintTheme.typography.body2R14,
                color = FlintTheme.colors.gray300,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    style = FlintTheme.typography.body1M16,
                    color = confirmColor,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "취소",
                    style = FlintTheme.typography.body1M16,
                    color = FlintTheme.colors.gray300,
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    FlintTheme {
        SettingScreen(
            uiState = SettingUiState(
                nickname = "한비두비세비",
                profileImageUrl = null,
                email = "flint@flint.com",
            ),
            onBackClick = {},
            onEditProfileClick = {},
            onLogoutClick = {},
            onWithdrawClick = {},
            onLogoutConfirm = {},
            onLogoutDismiss = {},
        )
    }
}
