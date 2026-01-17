package com.flint.data.mapper.auth

import com.flint.data.dto.auth.request.SignupRequestDto
import com.flint.data.dto.auth.response.SignupResponseDto
import com.flint.data.model.auth.SignupRequestModel
import com.flint.data.model.auth.SignupResponseModel

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