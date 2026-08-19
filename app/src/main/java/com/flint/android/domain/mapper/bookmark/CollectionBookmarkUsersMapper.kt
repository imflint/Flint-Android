package com.flint.android.domain.mapper.bookmark

import com.flint.android.data.dto.bookmark.CollectionBookmarkUsersDto
import com.flint.android.domain.model.bookmark.CollectionBookmarkUsersModel
import com.flint.android.domain.type.UserRoleType
import kotlinx.collections.immutable.toImmutableList

fun CollectionBookmarkUsersDto.toModel(): CollectionBookmarkUsersModel {
    return CollectionBookmarkUsersModel(
        bookmarkCount = bookmarkCount,
        userList = userList.map { it.toModel() }.toImmutableList()
    )
}

private fun CollectionBookmarkUsersDto.User.toModel(): CollectionBookmarkUsersModel.User {
    return CollectionBookmarkUsersModel.User(
        nickName = nickName,
        profileImageUrl = profileImageUrl,
        userId = userId,
        userRole = runCatching { UserRoleType.valueOf(userRole) }.getOrDefault(UserRoleType.NONE),
    )
}