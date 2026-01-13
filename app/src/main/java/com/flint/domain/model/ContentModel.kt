package com.flint.domain.model

import com.flint.domain.type.OttType

data class ContentModel(
    val contentId: Long,
    val title: String,
    val year: Int,
    val posterImage: String,
    val ottSimpleList: List<OttType>
)
