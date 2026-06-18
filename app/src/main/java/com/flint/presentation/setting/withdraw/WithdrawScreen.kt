package com.flint.presentation.setting.withdraw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintLargeButton
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun WithdrawRoute(
    navigateUp: () -> Unit,
    navigateToWithdrawComplete: () -> Unit,
    viewModel: WithdrawViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigateToWithdrawComplete.collect { navigateToWithdrawComplete() }
    }

    WithdrawScreen(
        uiState = uiState,
        onBackClick = navigateUp,
        onToggleConfirm = viewModel::toggleConfirm,
        onWithdrawClick = viewModel::withdraw,
    )
}

@Composable
private fun WithdrawScreen(
    uiState: WithdrawUiState,
    onBackClick: () -> Unit,
    onToggleConfirm: () -> Unit,
    onWithdrawClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(FlintTheme.colors.background),
    ) {
        FlintBackTopAppbar(
            onClick = onBackClick,
            title = "탈퇴하기",
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 28.dp),
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "계정 삭제 시\nFlint에서 기록한 모든 정보가 사라져요",
                style = FlintTheme.typography.head1Sb22,
                color = FlintTheme.colors.white,
            )

            Spacer(modifier = Modifier.height(28.dp))

            WithdrawNoticeList()
        }

        Spacer(Modifier.height(28.dp))

        HorizontalDivider(
            color = FlintTheme.colors.gray600,
            thickness = 4.dp,
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable(onClick = onToggleConfirm)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "위 유의사항을 확인했습니다",
                style = FlintTheme.typography.body1M16,
                color = FlintTheme.colors.white,
                modifier = Modifier.weight(1f),
            )

            WithdrawCheckBox(checked = uiState.isConfirmed)
        }

        Spacer(modifier = Modifier.weight(1f))

        FlintLargeButton(
            text = "탈퇴하기",
            state = if (uiState.isConfirmed) FlintButtonState.Destructive else FlintButtonState.Disable,
            onClick = onWithdrawClick,
            enabled = uiState.isConfirmed && !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
        )
    }
}

@Composable
private fun WithdrawNoticeList(modifier: Modifier = Modifier) {
    val notices = listOf(
        "회원 탈퇴 시 즉시 탈퇴 처리되며, 서비스 이용이 제한됩니다.",
        "저장한 작품 및 등록한 컬렉션, 취향 키워드 등 모든 이용 기록이 삭제되며, 다시 복구할 수 없습니다.",
        "동일한 계정 정보로 재가입하더라도 이전의 이용 기록은 복원되지 않습니다.",
        "관련 법령에 따라 일부 정보는 일정 기간 보관될 수 있으며, 해당 정보는 법적 의무 이외의 목적으로 사용되지 않습니다.",
    )

    Column(modifier = modifier) {
        notices.forEachIndexed { index, text ->
            Row {
                Text(
                    text = "${index + 1}. ",
                    style = FlintTheme.typography.body1M16,
                    color = FlintTheme.colors.gray300,
                )
                Text(
                    text = text,
                    style = FlintTheme.typography.body1M16,
                    color = FlintTheme.colors.gray300,
                    modifier = Modifier.offset(y = (-3).dp)
                )
            }
        }
    }
}

@Composable
private fun WithdrawCheckBox(checked: Boolean) {
    Icon(
        imageVector = ImageVector.vectorResource(
            if (checked) R.drawable.ic_check_fill else R.drawable.ic_check_empty
        ),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier.size(48.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun WithdrawScreenEmptyPreview() {
    FlintTheme {
        WithdrawScreen(
            uiState = WithdrawUiState(isConfirmed = false),
            onBackClick = {},
            onToggleConfirm = {},
            onWithdrawClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WithdrawScreenFilledPreview() {
    FlintTheme {
        WithdrawScreen(
            uiState = WithdrawUiState(isConfirmed = true),
            onBackClick = {},
            onToggleConfirm = {},
            onWithdrawClick = {},
        )
    }
}
