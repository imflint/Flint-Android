package com.flint.core.common.di.interceptor

import com.flint.core.common.datastore.PreferencesManager
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
            "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjgwMDAyMTcxMTM1NzExODE5MSwicm9sZSI6IkZMSU5HIiwidHlwZSI6IkFDQ0VTUyIsImlhdCI6MTc2ODczODA2OSwiZXhwIjoxNzY5OTQ3NjY5fQ.1CnSivZUkVPTzBqLzxjMnc6AzooxuQ2v_saX9tyLqIuR-YEGMX2yl2UfVqQNrMKtvtAUewi1BfziBh1j9-GQ9g"

//            val accessToken =
//                runBlocking {
//                    preferencesManager.getString(ACCESS_TOKEN).first()
//                }

        val requestBuilder = originalRequest.newBuilder()

        if (accessToken.isNotEmpty()) {
            requestBuilder.header("Authorization", "Bearer $accessToken")
        }

        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}
