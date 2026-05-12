package com.flint.domain.model.storage

data class PresignedUrlModel(
    val uploadUrl: String,
    val key: String,
)
