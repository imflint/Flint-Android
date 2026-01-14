package com.flint.presentation.login.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.core.designsystem.theme.FlintTypography

@Composable
fun KakaoLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .height(48.dp)
                .clickable { onClick() }
                .clip(RoundedCornerShape(8.dp))
                .background(FlintTheme.colors.kakao)
                .padding(horizontal = 24.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_kakao),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterStart),
        )

        Text(
            text = "카카오로 시작하기",
            style = FlintTypography.body1M16,
            color = FlintTheme.colors.background,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Preview
@Composable
private fun PreviewKakaoLoginButton() {
    FlintTheme {
        KakaoLoginButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
