package com.flint.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.dropShadow
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.login.component.KakaoLoginButton

@Composable
fun LoginRoute(
    paddingValues: PaddingValues,
    navigateToOnBoarding: () -> Unit,
    navigateToHome: () -> Unit,
) {
    LoginScreen(modifier = Modifier.padding(paddingValues))
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(FlintTheme.colors.gradient900)
                .dropShadow(
                    shape = RectangleShape,
                    color = Color(0xFF000000).copy(alpha = 0.25f),
                    offsetX = 0.dp,
                    offsetY = 4.dp,
                    blur = 4.dp,
                    spread = 0.dp,
                ),
    ) {
        Image(
            painter = painterResource(R.drawable.img_flint_title),
            contentDescription = null,
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .width(160.dp)
                    .height(56.dp)
                    .offset(y = (-32).dp),
        )

        KakaoLoginButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 60.dp)
                    .padding(horizontal = 16.dp),
            onClick = {
                // TODO Kakao Login
            },
        )
    }
}

@Preview
@Composable
private fun PreviewLoginScreen() {
    FlintTheme {
        LoginScreen()
    }
}
