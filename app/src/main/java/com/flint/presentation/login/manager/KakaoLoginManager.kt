package com.flint.presentation.login.manager

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject

class KakaoLoginManager @Inject constructor() {
    fun login(
        context: Context,
        onResult: (Result<OAuthToken>) -> Unit
    ) {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) return@loginWithKakaoTalk
                when {
                    token != null -> onResult(Result.success(token))
                    error != null -> UserApiClient.instance.loginWithKakaoAccount(context) { token2, error2 ->
                        if (token2 != null) {
                            onResult(Result.success(token2))
                        } else {
                            onResult(Result.failure(error2 ?: Exception("Unknown error")))
                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                if (token != null) {
                    onResult(Result.success(token))
                } else {
                    onResult(Result.failure(error ?: Exception("Unknown error")))
                }
            }
        }
    }
}