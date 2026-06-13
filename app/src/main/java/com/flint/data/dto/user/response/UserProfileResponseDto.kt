package com.flint.data.dto.user.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponseDto(
    @SerialName("id")
    val id: String,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("isFliner")
    val isFliner: Boolean,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("keywordRecalculatable")
    val keywordRecalculatable: Boolean = false,
)
