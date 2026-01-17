package com.flint.core.common.di.interceptor

import com.flint.core.common.datastore.PreferencesManager
import com.flint.core.common.util.DataStoreKey.ACCESS_TOKEN
import com.flint.core.common.util.DataStoreKey.REFRESH_TOKEN
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val preferencesManager: PreferencesManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken =
            runBlocking {
                preferencesManager.getString(ACCESS_TOKEN).first()
            }

        val refreshToken =
            runBlocking {
                preferencesManager.getString(REFRESH_TOKEN).first()
            }

        val requestBuilder = originalRequest.newBuilder()

        if (accessToken.isNotEmpty()) {
            requestBuilder.header("accesstoken", accessToken)
        }

        if (refreshToken.isNotEmpty()) {
            requestBuilder.header("refreshtoken", refreshToken)
        }

        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}