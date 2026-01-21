package com.flint.domain.model.ott

data class OttListModel(
    val otts: List<OttModel> = emptyList()
)

data class OttModel(
    val ottId: String = "",
    val name: String = "",
    val logoUrl: String = "",
    val contentUrl: String = ""
)
