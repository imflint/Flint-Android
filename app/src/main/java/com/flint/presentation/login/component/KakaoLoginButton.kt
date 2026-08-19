package com.flint.presentation.login.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.interaction.pressClickable
import com.flint.core.designsystem.interaction.pressScale
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.core.designsystem.theme.FlintTypography

@Composable
fun KakaoLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier =
            modifier
                .height(48.dp)
                .pressScale(interactionSource)
                .clip(RoundedCornerShape(8.dp))
                .background(FlintTheme.colors.kakao)
                .pressClickable(
                    interactionSource = interactionSource,
                    role = Role.Button,
                    onClick = onClick,
                )
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
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 8.dp),
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
