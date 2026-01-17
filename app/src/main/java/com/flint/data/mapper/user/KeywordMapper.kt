package com.flint.data.mapper.user

import com.flint.data.dto.user.response.UserKeywordsResponseDto
import com.flint.data.dto.user.response.UserKeywordsResultDto
import com.flint.data.model.user.UserKeywordListModel
import com.flint.data.model.user.UserKeywordModel

fun UserKeywordsResponseDto.toModel(): UserKeywordListModel =
    UserKeywordListModel(
        keywordList =
            keywords.map { dto ->
                dto.toModel()
            },
    )

private fun UserKeywordsResultDto.toModel(): UserKeywordModel =
    UserKeywordModel(
        color = color,
        rank = rank,
        name = name,
        percentage = percentage,
        imageUrl = imageUrl,
    )
