package com.flint.data.model.user

import com.flint.core.common.type.UserRoleType

data class AuthorModel(
    val userId: Long,
    val nickname: String,
    val profileUrl: String,
    val userRole: UserRoleType,
) {
    companion object {
        val Fake =
            AuthorModel(
                userId = 1L,
                nickname = "닉네임",
                profileUrl = "",
                userRole = UserRoleType.FLINER,
            )
    }
}
