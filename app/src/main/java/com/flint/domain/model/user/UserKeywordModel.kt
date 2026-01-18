package com.flint.domain.model.user

data class UserKeywordListModel(
    val keywordList: List<UserKeywordModel>,
)

data class UserKeywordModel(
    val color: String,
    val rank: Int,
    val name: String,
    val percentage: Float,
    val imageUrl: String,
)
