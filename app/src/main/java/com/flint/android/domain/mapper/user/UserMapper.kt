package com.flint.android.domain.mapper.user

import com.flint.android.data.dto.user.response.NicknameCheckResponseDto
import com.flint.android.domain.model.user.NicknameCheckModel

fun NicknameCheckResponseDto.toModel(): NicknameCheckModel {
    return NicknameCheckModel(
        isAvailable = this.available
    )
}