package com.flint.domain.mapper.auth

import com.flint.data.dto.auth.request.SignupRequestDto
import com.flint.data.dto.auth.response.SignupResponseDto
import com.flint.domain.model.auth.SignupRequestModel
import com.flint.domain.model.auth.SignupResponseModel

fun SignupRequestModel.toDto(): SignupRequestDto =
    SignupRequestDto(
        tempToken = tempToken,
        nickname = nickname,
        favoriteContentIds = favoriteContentIds,
        subscribedOttIds = subscribedOttIds,
    )

fun SignupResponseDto.toModel(): SignupResponseModel =
    SignupResponseModel(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
    )
