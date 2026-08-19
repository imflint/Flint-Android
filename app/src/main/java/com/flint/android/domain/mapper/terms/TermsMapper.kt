package com.flint.android.domain.mapper.terms

import com.flint.android.data.dto.terms.response.TermResponseDto
import com.flint.android.domain.model.terms.TermModel

fun TermResponseDto.toModel(): TermModel =
    TermModel(
        id = id,
        type = type,
        version = version,
        title = title,
        content = content,
        required = required,
        activeAt = activeAt,
    )

fun List<TermResponseDto>.toModel(): List<TermModel> = map { it.toModel() }
