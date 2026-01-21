package com.flint.domain.mapper.user

import com.flint.data.dto.user.response.NicknameCheckResponseDto
import com.flint.domain.model.user.NicknameCheckModel

fun NicknameCheckResponseDto.toModel(): NicknameCheckModel {
    return NicknameCheckModel(
        isAvailable = this.available
    )
}