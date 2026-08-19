package com.flint.android.domain.mapper.storage

import com.flint.android.data.dto.storage.response.PresignedUrlResponseDto
import com.flint.android.domain.model.storage.PresignedUrlModel

fun PresignedUrlResponseDto.toModel(): PresignedUrlModel =
    PresignedUrlModel(
        uploadUrl = uploadUrl,
        key = key,
    )
