package com.flint.android.data.dto.terms.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TermsListResponseDto(
    @SerialName("terms")
    val terms: List<TermResponseDto>,
)
