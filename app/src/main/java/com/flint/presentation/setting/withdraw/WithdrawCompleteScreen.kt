package com.flint.presentation.setting.withdraw

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintLargeButton
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun WithdrawCompleteRoute(
    navigateToLogin: () -> Unit,
) {
    WithdrawCompleteScreen(
        onNavigateToLogin = navigateToLogin,
    )
}

@Composable
private fun WithdrawCompleteScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(FlintTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.ic_gradient_check),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "회원 탈퇴 완료",
            style = FlintTheme.typography.display2M28,
            color = FlintTheme.colors.white,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "그동안 Flint를 이용해주셔서\n진심으로 감사 드립니다",
            style = FlintTheme.typography.body1M16,
            color = FlintTheme.colors.white,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(1f))

        FlintLargeButton(
            text = "첫 화면으로 이동하기",
            state = FlintButtonState.Able,
            onClick = onNavigateToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WithdrawCompleteScreenPreview() {
    FlintTheme {
        WithdrawCompleteScreen(
            onNavigateToLogin = {},
        )
    }
}
