package com.flint.domain.model

data class ContentModel(
    val contentId: Long,
    val title: String,
    val year: Int,
    val posterImage: String,
    val ottSimpleList: List<OttModel>
)
