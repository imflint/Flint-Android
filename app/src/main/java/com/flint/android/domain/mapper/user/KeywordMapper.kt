package com.flint.android.domain.mapper.user

import com.flint.android.data.dto.user.response.UserKeywordsResponseDto
import com.flint.android.data.dto.user.response.UserKeywordsResultDto
import com.flint.android.domain.model.user.KeywordItemModel
import com.flint.android.domain.model.user.KeywordListModel
import kotlinx.collections.immutable.toImmutableList

fun UserKeywordsResponseDto.toModel(): KeywordListModel =
    KeywordListModel(
        keywords = keywords.map { it.toModel() }.toImmutableList(),
    )

fun UserKeywordsResultDto.toModel(): KeywordItemModel =
    KeywordItemModel(
        color = color,
        rank = rank,
        name = name,
        percentage = percentage,
        imageUrl = imageUrl,
    )
