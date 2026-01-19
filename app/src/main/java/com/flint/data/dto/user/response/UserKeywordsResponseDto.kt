package com.flint.data.dto.user.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserKeywordsResponseDto(
    @SerialName("keywords")
    val keywords: List<UserKeywordsResultDto>,
)

@Serializable
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
