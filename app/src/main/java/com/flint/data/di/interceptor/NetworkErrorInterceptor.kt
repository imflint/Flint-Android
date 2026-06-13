package com.flint.data.di.interceptor

import com.flint.data.dto.base.ErrorResponseDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class NetworkErrorInterceptor @Inject constructor(
    private val networkErrorManager: NetworkErrorManager,
    private val json: Json,
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
                        val isBusinessError = response.isBusinessError()
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

    private fun Response.isBusinessError(): Boolean {
        val body = runCatching { peekBody(ERROR_BODY_PEEK_BYTES).string() }.getOrNull()
            ?: return false

        return runCatching {
            json.decodeFromString<ErrorResponseDto>(body).errorCode != null
        }.getOrDefault(false)
    }

    private companion object {
        const val ERROR_BODY_PEEK_BYTES = 64 * 1024L
    }
}
