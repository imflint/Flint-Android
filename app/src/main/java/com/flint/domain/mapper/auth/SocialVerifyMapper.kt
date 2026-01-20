package com.flint.domain.mapper.auth

import com.flint.data.dto.auth.request.SocialVerifyRequestDto
import com.flint.data.dto.auth.response.SocialVerifyResponseDto
import com.flint.domain.model.auth.SocialVerifyRequestModel
import com.flint.domain.model.auth.SocialVerifyResponseModel

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
        userName = userName,
        tempToken = tempToken,
    )
