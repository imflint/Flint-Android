package com.flint.domain.mapper.user

import com.flint.data.dto.user.response.UserKeywordsResultDto
import com.flint.domain.model.user.UserKeywordResponseModel

fun UserKeywordsResultDto.toModel(): UserKeywordResponseModel =
    UserKeywordResponseModel(
        color = color,
        rank = rank,
        name = name,
        percentage = percentage,
        imageUrl = imageUrl,
    )
