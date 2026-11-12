package com.flint.android.data.di.interceptor

import com.flint.android.data.local.PreferencesManager
import com.flint.android.core.common.util.DataStoreKey.ACCESS_TOKEN
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor
    @Inject
    constructor(
        private val preferencesManager: PreferencesManager,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()

            val accessToken =
                runBlocking {
                    preferencesManager.getString(ACCESS_TOKEN).first()
                }

            val requestBuilder = originalRequest.newBuilder()

            val isPublicEndpoint = originalRequest.url.encodedPath in PUBLIC_ENDPOINTS
            if (accessToken.isNotEmpty() && !isPublicEndpoint) {
                requestBuilder.header("Authorization", "Bearer $accessToken")
            }

            val request = requestBuilder.build()


            return chain.proceed(request)
        }
    }

// 인증 없이 호출하는 엔드포인트. 만료된 토큰이 실려 가면 서버 인증 필터에서 막힐 수 있어 토큰을 싣지 않는다.
private val PUBLIC_ENDPOINTS = setOf(
    "/api/v1/search/contents",
    "/api/v1/system/version",
)
