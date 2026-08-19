package com.flint.android.domain.mapper.ott

import com.flint.android.data.dto.ott.response.OttItemResponseDto
import com.flint.android.data.dto.ott.response.OttListResponseDto
import com.flint.android.domain.model.ott.OttListModel
import com.flint.android.domain.model.ott.OttModel

fun OttListResponseDto.toModel() : OttListModel {
    return OttListModel(
        otts = otts.map { it.toModel() }
    )
}

fun OttItemResponseDto.toModel() : OttModel {
    return OttModel(
        ottId = ottId,
        name = name,
        logoUrl = logoUrl,
        contentUrl = contentUrl
    )
}