package com.flint.android.domain.model.collection

data class CollectionReportRequestModel(
    val reasons: List<String>,
    val otherDetail: String? = null,
)
