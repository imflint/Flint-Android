package com.flint.domain.model.collection

import com.flint.domain.model.AuthorModelNew
import com.flint.domain.model.content.ContentModelNew
import kotlinx.collections.immutable.ImmutableList
import java.time.LocalDate

data class CollectionDetailModelNew(
    val author: AuthorModelNew,
    val contents: ImmutableList<ContentModelNew>,
    val createdAt: LocalDate,
    val description: String,
    val id: String,
    val thumbnailUrl: String,
    val isBookmarked: Boolean,
    val title: String,
)
