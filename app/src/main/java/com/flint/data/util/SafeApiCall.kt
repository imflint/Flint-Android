package com.flint.data.util

inline fun <T> safeApiCall(block: () -> T): Result<T> =
    runCatching { block() }
        .fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it) },
        )
