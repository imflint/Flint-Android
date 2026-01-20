package com.flint.domain.mapper.bookmark

import com.flint.data.dto.bookmark.CollectionBookmarkUsersDto
import com.flint.domain.model.bookmark.CollectionBookmarkUsersModel
import com.flint.domain.type.UserRoleType
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