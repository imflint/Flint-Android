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

            val isPublicEndpoint = originalRequest.url.encodedPath == "/api/v1/search/contents"
            if (accessToken.isNotEmpty() && !isPublicEndpoint) {
                requestBuilder.header("Authorization", "Bearer $accessToken")
            }

            val request = requestBuilder.build()


            return chain.proceed(request)
        }
    }
