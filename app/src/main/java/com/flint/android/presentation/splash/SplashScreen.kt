package com.flint.android.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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

    // 로고 레이어가 사라지는 빈 프레임 전까지만 재생 (로티 파일 교체 대비, endFrame에서 유도)
    val logoOutFrame = composition?.endFrame?.toInt()
    val animationState = animateLottieCompositionAsState(
        composition = composition,
        clipSpec = logoOutFrame?.let { LottieClipSpec.Frame(max = it, maxInclusive = false) },
    )

    // composition도 key에 포함: 애니메이터 배율 0 설정에서 isAtEnd가 처음부터 true로 스냅되는 경우 대비
    LaunchedEffect(composition, animationState.isAtEnd) {
        if (composition != null && animationState.isAtEnd) {
            onAnimationFinished()
        }
    }

    // 화면 진입 시점부터 고정 상한 (composition 로드 지연과 무관하게 최대 대기시간 보장)
    LaunchedEffect(Unit) {
        delay(FALLBACK_TIMEOUT_MILLIS)
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

// progress 콜백 누락 시 대비용 최대 대기시간. 정상적으로는 isAtEnd가 먼저 호출됨
private const val FALLBACK_TIMEOUT_MILLIS = 4000L

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    FlintTheme {
        SplashScreen(
            onAnimationFinished = {},
        )
    }
}
