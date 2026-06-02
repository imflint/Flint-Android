package com.flint.data.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequestDto(
    @SerialName("tempToken")
    val tempToken: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("favoriteContentIds")
    val favoriteContentIds: List<String>,
    @SerialName("agreedTermsIds")
    val agreedTermsIds: List<String>,
    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
)
