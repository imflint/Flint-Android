package com.flint.data.mapper.auth

import com.flint.data.dto.request.auth.SignupRequestDto
import com.flint.data.dto.response.auth.SignupResponseDto
import com.flint.domain.model.auth.SignupRequestModel
import com.flint.domain.model.auth.SignupResponseModel

fun SignupRequestModel.toDto(): SignupRequestDto {
    return SignupRequestDto(
        tempToken = tempToken,
        nickname = nickname,
        favoriteContentIds = favoriteContentIds,
        subscribedOttIds = subscribedOttIds
    )
}

fun SignupResponseDto.toModel(): SignupResponseModel {
    return SignupResponseModel(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId
    )
}