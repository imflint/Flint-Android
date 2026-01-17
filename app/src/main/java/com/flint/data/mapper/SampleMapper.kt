package com.flint.data.mapper

import com.flint.data.dto.SampleResponseDto
import com.flint.domain.model.SampleModel

fun SampleResponseDto.toModel(): SampleModel =
    SampleModel(
        convertedData = data.toInt(),
    )
