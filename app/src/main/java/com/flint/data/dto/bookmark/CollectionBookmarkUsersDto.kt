package com.flint.data.dto.bookmark


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionBookmarkUsersDto(
    @SerialName("bookmarkCount") val bookmarkCount: Int,
    @SerialName("userList") val userList: List<User>,
) {
    @Serializable
    data class User(
        @SerialName("nickName") val nickName: String,
        @SerialName("profileImageUrl") val profileImageUrl: String?,
        @SerialName("userId") val userId: String,
        @SerialName("userRole") val userRole: String,
    )
}

