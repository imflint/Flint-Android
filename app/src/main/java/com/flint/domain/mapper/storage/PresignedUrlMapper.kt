package com.flint.domain.mapper.storage

import com.flint.data.dto.storage.response.PresignedUrlResponseDto
import com.flint.domain.model.storage.PresignedUrlModel

fun PresignedUrlResponseDto.toModel(): PresignedUrlModel =
    PresignedUrlModel(
        uploadUrl = uploadUrl,
        key = key,
    )
