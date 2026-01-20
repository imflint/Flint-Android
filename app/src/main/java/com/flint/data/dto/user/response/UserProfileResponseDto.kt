package com.flint.data.dto.user.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponseDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("profileImageUrl")
    val profileImageUrl: String?,
    @SerializedName("isFliner")
    val isFliner: Boolean,
    @SerializedName("nickname")
    val nickname: String
)