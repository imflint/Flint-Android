package com.flint.data.dto.collection.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionReportRequestDto(
    @SerialName("reasons")
    val reasons: List<String>,
    @SerialName("otherDetail")
    val otherDetail: String? = null,
)
