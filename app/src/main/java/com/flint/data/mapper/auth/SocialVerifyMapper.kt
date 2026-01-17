package com.flint.data.mapper.auth

import com.flint.data.dto.auth.request.SocialVerifyRequestDto
import com.flint.data.dto.auth.response.SocialVerifyResponseDto
import com.flint.data.model.auth.SocialVerifyRequestModel
import com.flint.data.model.auth.SocialVerifyResponseModel

fun SocialVerifyRequestModel.toDto() : SocialVerifyRequestDto {
    return SocialVerifyRequestDto(
        provider = provider.name,
        accessToken = accessToken
    )
}

fun SocialVerifyResponseDto.toModel() : SocialVerifyResponseModel {
    return SocialVerifyResponseModel(
        isRegistered = isRegistered,
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
        tempToken = tempToken
    )
}