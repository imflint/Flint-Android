package com.flint.domain.mapper.user

import com.flint.data.dto.user.response.UserKeywordsResponseDto
import com.flint.data.dto.user.response.UserKeywordsResultDto
import com.flint.domain.model.user.UserKeywordListModel
import com.flint.domain.model.user.UserKeywordModel

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
// Activity(UiState -> Composable) -> ViewModel(Dto -> UiState) -> Repository(Dto -> Model) -> API(JSON -> Dto)
// viewModelScope.launch { runCatching { Service.getXXX }.onS, on }
