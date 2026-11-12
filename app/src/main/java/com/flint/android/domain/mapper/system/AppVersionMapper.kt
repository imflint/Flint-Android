package com.flint.android.domain.mapper.system

import com.flint.android.data.dto.system.response.AppVersionResponseDto
import com.flint.android.domain.model.system.AppVersionModel

fun AppVersionResponseDto.toModel(): AppVersionModel =
    AppVersionModel(
        minimumVersion = version,
        storeUrl = path,
    )
