package com.flint.android.domain.mapper.auth

import com.flint.android.data.dto.auth.request.SignupRequestDto
import com.flint.android.data.dto.auth.response.SignupResponseDto
import com.flint.android.domain.model.auth.SignupRequestModel
import com.flint.android.domain.model.auth.SignupResponseModel

fun SignupRequestModel.toDto(): SignupRequestDto =
    SignupRequestDto(
        tempToken = tempToken,
        nickname = nickname,
        favoriteContentIds = favoriteContentIds,
        agreedTermsIds = agreedTermsIds,
        profileImageUrl = profileImageUrl,
    )

fun SignupResponseDto.toModel(): SignupResponseModel =
    SignupResponseModel(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
    )
