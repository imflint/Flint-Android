package com.flint.data.mapper.auth

import com.flint.data.dto.request.auth.SocialVerifyRequestDto
import com.flint.data.dto.response.auth.SocialVerifyResponseDto
import com.flint.domain.model.auth.SocialVerifyRequestModel
import com.flint.domain.model.auth.SocialVerifyResponseModel

fun SocialVerifyRequestModel.toDto() : SocialVerifyRequestDto {
    return SocialVerifyRequestDto(
        provider = provider.name,
        code = code
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