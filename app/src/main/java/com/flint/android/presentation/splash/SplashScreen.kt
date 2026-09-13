package com.flint.android.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.flint.android.R
import com.flint.android.core.designsystem.theme.FlintTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun SplashRoute(
    paddingValues: PaddingValues,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val autoLoginState by viewModel.autoLoginState.collectAsStateWithLifecycle()
    var isAnimationFinished by remember { mutableStateOf(false) }

    SplashScreen(
        onAnimationFinished = { isAnimationFinished = true },
    )

    LaunchedEffect(isAnimationFinished, autoLoginState) {
        if (isAnimationFinished && autoLoginState != AutoLoginState.Loading) {
            when (autoLoginState) {
                AutoLoginState.NavigateToHome -> navigateToHome()
                AutoLoginState.NavigateToLogin -> navigateToLogin()
                AutoLoginState.Loading -> Unit
            }
        }
    }
}

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.flint_lottie))

    // 로티 마지막 프레임(레이어 op = 90)에서 로고 레이어가 화면에서 빠져 빈 화면이 된다.
    // 그 상태로 로그인 화면과 크로스페이드되면 로고가 한 번 깜빡이므로,
    // 로고가 아직 남아있는 프레임까지만 재생해 그 화면 그대로 로그인 화면으로 넘긴다.
    val animationState = animateLottieCompositionAsState(
        composition = composition,
        clipSpec = LottieClipSpec.Frame(max = LOGO_LAYER_OUT_FRAME, maxInclusive = false),
    )

    // 애니메이션이 끝까지 재생되면 콜백 실행
    LaunchedEffect(key1 = animationState.isAtEnd) {
        if (composition != null && animationState.isAtEnd) {
            onAnimationFinished()
        }
    }

    // progress 콜백이 오지 않는 예외 상황에 대비
    LaunchedEffect(key1 = composition) {
        val timeoutMillis = composition
            ?.duration
            ?.toLong()
            ?.plus(ANIMATION_TIMEOUT_MARGIN_MILLIS)
            ?: COMPOSITION_LOAD_TIMEOUT_MILLIS
        delay(timeoutMillis)
        onAnimationFinished()
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(FlintTheme.colors.background),
        contentAlignment = Alignment.Center,
    ) {
        LottieAnimation(
            composition = composition,
            progress = { animationState.progress },
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

// 로티에서 로고 레이어가 화면에서 빠지는 프레임 (flint_lottie.json 의 로고 레이어 op)
private const val LOGO_LAYER_OUT_FRAME = 90

// 로티 재생이 끝난 뒤 progress 콜백을 기다려주는 여유 시간
private const val ANIMATION_TIMEOUT_MARGIN_MILLIS = 500L

// 로티 컴포지션 로드에 실패했을 때 스플래시에 머무는 최대 시간
private const val COMPOSITION_LOAD_TIMEOUT_MILLIS = 2000L

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    FlintTheme {
        SplashScreen(
            onAnimationFinished = {},
        )
    }
}
