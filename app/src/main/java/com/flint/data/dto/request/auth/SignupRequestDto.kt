package com.flint.data.dto.request.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequestDto(
    @SerialName("tempToken")
    val tempToken: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("favoriteContentIds")
    val favoriteContentIds: List<Long>,
    @SerialName("subscribedOttIds")
    val subscribedOttIds: List<Long>
)