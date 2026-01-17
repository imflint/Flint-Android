package com.flint.data.dto.user.response

import kotlinx.serialization.SerialName

data class UserKeywordsResponseDto(
    @SerialName("keywords")
    val keywords: List<UserKeywordsResultDto>,
)

data class UserKeywordsResultDto(
    @SerialName("color")
    val color: String,
    @SerialName("rank")
    val rank: Int,
    @SerialName("name")
    val name: String,
    @SerialName("percentage")
    val percentage: Float,
    @SerialName("imageUrl")
    val imageUrl: String,
)
