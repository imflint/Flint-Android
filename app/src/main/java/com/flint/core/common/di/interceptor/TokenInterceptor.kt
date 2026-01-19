package com.flint.core.common.di.interceptor

import com.flint.core.common.datastore.PreferencesManager
import com.flint.core.common.util.DataStoreKey.ACCESS_TOKEN
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

            if (accessToken.isNotEmpty()) {
                requestBuilder.header("Authorization", "Bearer $accessToken")
            }
            // sample
            requestBuilder.header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjgwMDAyMTcxMTM1NzExODE5MSwicm9sZSI6IkZMSU5HIiwidHlwZSI6IkFDQ0VTUyIsImlhdCI6MTc2ODgzODU0MCwiZXhwIjoxNzcwMDQ4MTQwfQ.A2XYyu24IoGAXNSQHJ1S-iudWmg8II2_ivI4EdyyWw9KS9oJlxHKOAhcKrsLpLkc9kllZyxwaTJO1t4vI7oZlg")

            val request = requestBuilder.build()

            return chain.proceed(request)
        }
    }
