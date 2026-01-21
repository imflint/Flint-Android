package com.flint.domain.mapper.user

import com.flint.data.dto.user.response.UserProfileResponseDto
import com.flint.domain.model.user.UserProfileResponseModel

fun UserProfileResponseDto.toModel(): UserProfileResponseModel =
    UserProfileResponseModel(
        id = id,
        isFliner = isFliner,
        nickname = nickname,
        profileImageUrl = profileImageUrl
    )