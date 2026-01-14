package com.flint.domain.model

import com.flint.domain.type.OttType

data class ContentModel(
    val contentId: Long,
    val title: String,
    val year: Int,
    val posterImage: String,
    val ottSimpleList: List<OttType>,
    val director: String = "",
    val isBookmarked: Boolean = false,
    val bookmarkCount: Int = 0,
    val description: String = "",
    val isSpoiler: Boolean = false,
)
