package com.flint.android.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flint.android.R
import com.flint.android.core.common.extension.dropShadow
import com.flint.android.core.common.manager.KakaoLoginManager
import com.flint.android.core.common.util.UiState
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.model.auth.SocialVerifyRequestModel
import com.flint.android.domain.type.ProviderType
import com.flint.android.presentation.login.component.KakaoLoginButton
import com.flint.android.presentation.login.event.LoginNavigationEvent
import com.flint.android.presentation.login.data.VerifyStatusData
import com.flint.android.presentation.splash.SplashLogoMetrics
import timber.log.Timber
import kotlin.math.max

@Composable
fun LoginRoute(
    navigateToOnBoarding: (tempToken: String) -> Unit,
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
    kakaoLoginManager: KakaoLoginManager = KakaoLoginManager()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    when(val event = uiState.data) {
                        is LoginNavigationEvent.NavigateToHome -> {
                            navigateToHome()
                        }
                        is LoginNavigationEvent.NavigateToOnBoarding -> {
                            navigateToOnBoarding(event.tempToken)
                        }
                    }
                }
                else -> {}
            }
        }
    }

    // Splash -> Login 전환 시 로고 위치가 그대로 이어지도록, Splash와 동일하게
    // 화면 전체(엣지투엣지)를 기준으로 배치
    LoginScreen(
        onKakaoLoginClick = {
            kakaoLoginManager.login(context) { result ->
                result.onSuccess { token ->
                    viewModel.socialVerifyWithKakao(
                        requestModel = SocialVerifyRequestModel(
                            provider = ProviderType.KAKAO,
                            accessToken = token.accessToken
                        ),
                    )
                }.onFailure { error ->
                    Timber.e(error)
                }
            }
        },
    )
}

@Composable
fun LoginScreen(
    onKakaoLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
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
        // 스플래시-> 1440x3120 캔버스를 ContentScale.Crop + 중앙 정렬로 그림
        // 같은 계산을 그대로 재현해서 로고의 크기와 위치를 맞추면, 기기 화면 크기와 무관하게
        // 스플래시가 끝난 그 자리에 로그인 화면의 로고가 정확히 겹침
        val splashScale = max(
            maxWidth.value / SplashLogoMetrics.CANVAS_WIDTH,
            maxHeight.value / SplashLogoMetrics.CANVAS_HEIGHT,
        )
        val splashCanvasLeft = (maxWidth.value - SplashLogoMetrics.CANVAS_WIDTH * splashScale) / 2f
        val splashCanvasTop = (maxHeight.value - SplashLogoMetrics.CANVAS_HEIGHT * splashScale) / 2f

        Image(
            painter = painterResource(R.drawable.img_flint_title),
            contentDescription = null,
            modifier =
                Modifier
                    .absoluteOffset(
                        x = (splashCanvasLeft + SplashLogoMetrics.LOGO_LEFT * splashScale).dp,
                        y = (splashCanvasTop + SplashLogoMetrics.LOGO_TOP * splashScale).dp,
                    )
                    .width((SplashLogoMetrics.LOGO_WIDTH * splashScale).dp)
                    .height((SplashLogoMetrics.LOGO_HEIGHT * splashScale).dp),
        )

        KakaoLoginButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 60.dp)
                    .padding(horizontal = 20.dp),
            onClick = {
                onKakaoLoginClick()
            },
        )
    }
}


@Preview
@Composable
private fun PreviewLoginScreen() {
    FlintTheme {
        LoginScreen(
            onKakaoLoginClick = {}
        )
    }
}
