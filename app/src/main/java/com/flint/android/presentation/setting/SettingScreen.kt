package com.flint.android.presentation.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.Alignment
import com.flint.android.core.common.util.ExternalLinks
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.android.R
import com.flint.android.core.designsystem.interaction.flintCardClickable
import com.flint.android.core.designsystem.component.button.FlintButtonState
import com.flint.android.core.designsystem.component.button.FlintMediumButton
import com.flint.android.core.designsystem.component.image.ProfileImage
import com.flint.android.core.designsystem.component.modal.TwoButtonModal
import com.flint.android.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.android.core.designsystem.theme.FlintTheme

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
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        uiState.email?.let { email ->
                            Text(
                                text = email,
                                style = FlintTheme.typography.body2R14,
                                color = FlintTheme.colors.gray100,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false),
                            )
                        }
                        Image(
                            painter = painterResource(R.drawable.ic_kakao_full),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                },
            )

            HorizontalDivider(color = FlintTheme.colors.gray700, thickness = 1.dp)

            SettingMenuItem(
                label = "개인정보 정책",
                onClick = {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(ExternalLinks.PRIVACY_POLICY_URL))
                    )
                },
            )

            HorizontalDivider(color = FlintTheme.colors.gray700, thickness = 1.dp)

            SettingMenuItem(
                label = "이용약관",
                onClick = {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(ExternalLinks.TERMS_OF_SERVICE_URL))
                    )
                },
            )

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
                    .flintCardClickable(onClick = onWithdrawClick)
                    .padding(vertical = 16.dp),
            )
        }
    }

    if (uiState.isLogoutDialogVisible) {
        TwoButtonModal(
            message = "로그아웃 하시겠습니까?",
            cancelText = "취소",
            confirmText = "로그아웃",
            onConfirm = onLogoutConfirm,
            onDismiss = onLogoutDismiss,
            icon = R.drawable.ic_gradient_people,
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
    verticalPadding: Dp = 18.dp,
    onClick: () -> Unit = {},
    trailingContent: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .flintCardClickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 라벨을 가중치 없이 먼저 측정해 trailingContent가 길어도 밀려나지 않게 한다.
        // Spacer가 남는 폭을 흡수해 trailingContent는 우측에 붙는다.
        Text(
            text = label,
            style = FlintTheme.typography.body1M16,
            color = FlintTheme.colors.white,
            // trailingContent가 폭을 가득 채워도 라벨과 붙지 않도록 최소 간격을 확보한다
            modifier = Modifier.padding(end = 16.dp),
        )
        Spacer(Modifier.weight(1f))
        trailingContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    FlintTheme {
        SettingScreen(
            uiState = SettingUiState(
                nickname = "한비두비세비",
                profileImageUrl = null,
                email = "flint@kakao.com",
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
