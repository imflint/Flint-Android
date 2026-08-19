package com.flint.android.data.dto.user.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileImageRequestDto(
    @SerialName("profileImage")
    val profileImage: String,
)
