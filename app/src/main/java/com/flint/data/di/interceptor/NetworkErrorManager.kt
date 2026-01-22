package com.flint.data.di.interceptor

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkErrorManager @Inject constructor() {
    private val _networkError = MutableSharedFlow<NetworkError>()
    val networkError = _networkError.asSharedFlow()

    suspend fun emitError(error: NetworkError) {
        _networkError.emit(error)
    }
}

sealed class NetworkError {
    data object NoInternet : NetworkError()
    data object Timeout : NetworkError()
    data class ConnectionError(val code: Int, val message: String?) : NetworkError()
    data class UnknownError(val message: String?) : NetworkError()
}
