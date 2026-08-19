package com.flint.android.domain.mapper.collection

import com.flint.android.data.dto.collection.response.CollectionDetailResponseDto
import com.flint.android.domain.model.AuthorModelNew
import com.flint.android.domain.model.collection.CollectionDetailModelNew
import com.flint.android.domain.model.content.ContentModelNew
import com.flint.android.domain.type.UserRoleType
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate

fun CollectionDetailResponseDto.toModel(): CollectionDetailModelNew {
    return CollectionDetailModelNew(
        author = author.toModel(),
        contents = contents.map { it.toModel() }.toImmutableList(),
        createdAt = LocalDate.parse(createdAt),
        description = description,
        id = id,
        thumbnailUrl = thumbnailUrl,
        isBookmarked = isBookmarked,
        isPublic = isPublic,
        title = title,
    )
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
        customImageUrls = customImageUrls,
        title = title,
        year = year,
    )
}
