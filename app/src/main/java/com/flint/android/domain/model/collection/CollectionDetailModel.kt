package com.flint.android.domain.model.collection

import com.flint.android.domain.model.AuthorModelNew
import com.flint.android.domain.model.content.ContentModelNew
import kotlinx.collections.immutable.ImmutableList
import java.time.LocalDate

data class CollectionDetailModelNew(
    val author: AuthorModelNew,
    val contents: ImmutableList<ContentModelNew>,
    val createdAt: LocalDate,
    val description: String,
    val id: String,
    val thumbnailUrl: String?,
    val isBookmarked: Boolean,
    val isPublic: Boolean,
    val title: String,
)
