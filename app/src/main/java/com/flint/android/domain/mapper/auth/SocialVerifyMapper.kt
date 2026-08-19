package com.flint.android.domain.mapper.auth

import com.flint.android.data.dto.auth.request.SocialVerifyRequestDto
import com.flint.android.data.dto.auth.response.SocialVerifyResponseDto
import com.flint.android.domain.model.auth.SocialVerifyRequestModel
import com.flint.android.domain.model.auth.SocialVerifyResponseModel

fun SocialVerifyRequestModel.toDto(): SocialVerifyRequestDto =
    SocialVerifyRequestDto(
        provider = provider.name,
        accessToken = accessToken,
    )

fun SocialVerifyResponseDto.toModel(): SocialVerifyResponseModel =
    SocialVerifyResponseModel(
        isRegistered = isRegistered,
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
        nickName = nickname,
        tempToken = tempToken,
    )
