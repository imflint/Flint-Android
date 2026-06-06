package com.flint.data.di.interceptor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class NetworkErrorInterceptor @Inject constructor(
    private val networkErrorManager: NetworkErrorManager,
) : Interceptor {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return try {
            val response = chain.proceed(request)

            if (!response.isSuccessful) {
                when (response.code) {
                    in 300..599 -> {
                        // errorCode 필드가 있으면 앱 레이어에서 직접 처리하는 비즈니스 에러 — 글로벌 에러 emit 생략
                        val isBusinessError = response.peekBody(Long.MAX_VALUE)
                            .string()
                            .contains("\"errorCode\"")
                        if (!isBusinessError) {
                            scope.launch {
                                networkErrorManager.emitError(
                                    NetworkError.ConnectionError(
                                        code = response.code,
                                        message = response.message
                                    )
                                )
                            }
                        }
                    }
                }
            }

            response
        } catch (e: UnknownHostException) {
            if (!chain.call().isCanceled()) {
                scope.launch {
                    networkErrorManager.emitError(NetworkError.NoInternet)
                }
            }
            throw e
        } catch (e: SocketTimeoutException) {
            if (!chain.call().isCanceled()) {
                scope.launch {
                    networkErrorManager.emitError(NetworkError.Timeout)
                }
            }
            throw e
        } catch (e: IOException) {
            if (!chain.call().isCanceled()) {
                scope.launch {
                    networkErrorManager.emitError(NetworkError.UnknownError(e.message))
                }
            }
            throw e
        }
    }
}
