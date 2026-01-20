package com.flint.domain.model

import com.flint.domain.type.UserRoleType

data class AuthorModelNew(
    val id: String,
    val nickname: String,
    val profileUrl: String?,
    val userRole: UserRoleType,
)
