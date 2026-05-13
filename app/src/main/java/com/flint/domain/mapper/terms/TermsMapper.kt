package com.flint.domain.mapper.terms

import com.flint.data.dto.terms.response.TermResponseDto
import com.flint.domain.model.terms.TermModel

fun TermResponseDto.toModel(): TermModel =
    TermModel(
        id = id.toLong(),
        type = type,
        version = version,
        title = title,
        content = content,
        required = required,
        activeAt = activeAt,
    )

fun List<TermResponseDto>.toModel(): List<TermModel> = map { it.toModel() }
