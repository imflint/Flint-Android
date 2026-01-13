package com.flint.domain.model

import com.flint.domain.type.UserRoleType

data class AuthorModel(
    val userId: Long,
    val nickname: String,
    val profileUrl: String,
    val userRole: UserRoleType,
)
