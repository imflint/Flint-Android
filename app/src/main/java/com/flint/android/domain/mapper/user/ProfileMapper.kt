package com.flint.android.domain.mapper.user

import com.flint.android.data.dto.user.response.UserProfileResponseDto
import com.flint.android.domain.model.user.UserProfileResponseModel

fun UserProfileResponseDto.toModel(): UserProfileResponseModel =
    UserProfileResponseModel(
        id = id,
        isFliner = isFliner,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        email = email,
        keywordRecalculatable = keywordRecalculatable,
    )