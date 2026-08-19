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

    // 애니메이션이 완료되지 않을 경우를 대비한 fallback
    LaunchedEffect(Unit) {
        delay(2000)
        isAnimationFinished = true
    }

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

    // 애니메이션 상태 관리
    val progress by animateLottieCompositionAsState(composition)

    // 애니메이션이 완료되면 progress 1f 도달 -> 콜백 실행
    LaunchedEffect(key1 = progress) {
        if (composition != null && progress == 1.0f) {
            onAnimationFinished()
        }
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
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    FlintTheme {
        SplashScreen(
            onAnimationFinished = {},
        )
    }
}
