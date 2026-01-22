package com.flint.domain.model.collection

import com.flint.data.dto.collection.response.CollectionDetailResponseDto
import com.flint.domain.model.AuthorModelNew
import com.flint.domain.model.content.ContentModelNew
import com.flint.domain.model.user.AuthorModel
import com.flint.domain.type.UserRoleType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class CollectionDetailModelNew(
    val author: AuthorModelNew,
    val contents: ImmutableList<ContentModelNew>,
    val createdAt: String,
    val description: String,
    val id: String,
    val thumbnailUrl: String,
    val isBookmarked: Boolean,
    val title: String,
    private val userId: String,
) {
    constructor(
        collectionDetail: CollectionDetailResponseDto,
        userId: String,
    ) : this(
        author = collectionDetail.author.toModel(),
        contents = collectionDetail.contents.map { it.toModel() }.toImmutableList(),
        createdAt = collectionDetail.createdAt,
        description = collectionDetail.description,
        id = collectionDetail.id,
        thumbnailUrl = collectionDetail.thumbnailUrl,
        isBookmarked = collectionDetail.isBookmarked,
        title = collectionDetail.title,
        userId = userId
    )

    val isMine: Boolean = author.id == userId
}

private fun CollectionDetailResponseDto.Author.toModel(): AuthorModelNew {
    return AuthorModelNew(
        id = id,
        nickname = nickname,
        profileImageUrl = profileImageUrl,
        userRole = runCatching { UserRoleType.valueOf(userRole) }.getOrDefault(UserRoleType.NONE)
    )
}

private fun CollectionDetailResponseDto.Content.toModel(): ContentModelNew {
    return ContentModelNew(
        director = director,
        bookmarkCount = bookmarkCount,
        id = id,
        isBookmarked = isBookmarked,
        isSpoiler = isSpoiler,
        reason = reason,
        imageUrl = imageUrl,
        title = title,
        year = year,
    )
}
