package com.flint.domain.mapper.ott

import com.flint.data.dto.ott.response.OttItemResponseDto
import com.flint.data.dto.ott.response.OttListResponseDto
import com.flint.domain.model.ott.OttListModel
import com.flint.domain.model.ott.OttModel

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