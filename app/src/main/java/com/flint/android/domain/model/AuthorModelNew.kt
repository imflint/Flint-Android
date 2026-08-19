package com.flint.android.domain.model

import com.flint.android.domain.type.UserRoleType

data class AuthorModelNew(
    val id: String,
    val nickname: String,
    val profileImageUrl: String?,
    val userRole: UserRoleType,
)
