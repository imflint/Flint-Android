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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.R
import com.flint.core.common.extension.dropShadow
import com.flint.core.common.manager.KakaoLoginManager
import com.flint.core.common.util.UiState
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.auth.SocialVerifyRequestModel
import com.flint.domain.type.ProviderType
import com.flint.presentation.login.component.KakaoLoginButton
import com.flint.presentation.login.data.VerifyStatusData
import timber.log.Timber

@Composable
fun LoginRoute(
    paddingValues: PaddingValues,
    navigateToOnBoarding: (tempToken: String) -> Unit,
    navigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
    kakaoLoginManager: KakaoLoginManager = KakaoLoginManager(),
) {
    val context = LocalContext.current
    val socialVerifyStatus by viewModel.verifyStatus.collectAsStateWithLifecycle()

    LaunchedEffect(socialVerifyStatus) {
        when (socialVerifyStatus) {
            is UiState.Success -> {
                val data = (socialVerifyStatus as UiState.Success<VerifyStatusData>).data

                if (data.isRegistered) {
                    navigateToHome()
                } else {
                    navigateToOnBoarding(data.tempToken ?: "")
                }
            }

            else -> {}
        }
    }

    LoginScreen(
        onKakaoLoginClick = {
            kakaoLoginManager.login(context) { result ->
                result
                    .onSuccess { token ->
                        viewModel.socialVerifyWithKakao(
                            requestModel =
                                SocialVerifyRequestModel(
                                    provider = ProviderType.KAKAO,
                                    accessToken = token.accessToken,
                                ),
                        )
                    }.onFailure { error ->
                        Timber.e(error)
                    }
            }
        },
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun LoginScreen(
    onKakaoLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            onKakaoLoginClick = {},
        )
    }
}
