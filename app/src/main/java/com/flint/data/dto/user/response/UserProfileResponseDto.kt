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
    // 내 프로필(/users/me) 응답에만 존재, 이메일 미보유 시 null
    @SerialName("email")
    val email: String? = null,
    @SerialName("keywordRecalculatable")
    val keywordRecalculatable: Boolean = false,
)
