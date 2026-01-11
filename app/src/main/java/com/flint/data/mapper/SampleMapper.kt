package com.flint.data.mapper

import com.flint.data.dto.response.SampleResponseDto
import com.flint.domain.model.SampleModel

fun SampleResponseDto.toModel(): SampleModel {
    return SampleModel(
        convertedData = data.toInt()
    )
}
