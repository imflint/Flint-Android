package com.flint.android.domain.model.user

data class UserProfileResponseModel(
    val id: String,
    val isFliner: Boolean,
    val nickname: String,
    val profileImageUrl: String?,
    val email: String? = null,
    val keywordRecalculatable: Boolean = false,
) {
    companion object {
        val Empty = UserProfileResponseModel(
            id = "",
            isFliner = false,
            nickname = "",
            profileImageUrl = ""
        )
        val Fake = UserProfileResponseModel(
            id = "123",
            isFliner = true,
            nickname = "닉네임",
            profileImageUrl = ""
        )
    }
}
