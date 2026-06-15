package com.flint.domain.model.collection

data class CollectionReportRequestModel(
    val reasons: List<String>,
    val otherDetail: String? = null,
)
