package com.flint.domain.model.bookmark

import com.flint.domain.type.UserRoleType
import kotlinx.collections.immutable.ImmutableList

data class CollectionBookmarkUsersModel(
    val bookmarkCount: Int,
    val userList: ImmutableList<User>,
) {
    data class User(
        val nickName: String,
        val profileImageUrl: String,
        val userId: String,
        val userRole: UserRoleType,
    )
}