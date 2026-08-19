package com.flint.android.domain.model.bookmark

import com.flint.android.domain.type.UserRoleType
import kotlinx.collections.immutable.ImmutableList

data class CollectionBookmarkUsersModel(
    val bookmarkCount: Int,
    val userList: ImmutableList<User>,
) {
    data class User(
        val nickName: String,
        val profileImageUrl: String?,
        val userId: String,
        val userRole: UserRoleType,
    )
}