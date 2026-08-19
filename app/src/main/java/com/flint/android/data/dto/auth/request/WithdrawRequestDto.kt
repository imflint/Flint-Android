package com.flint.android.data.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WithdrawRequestDto(
    @SerialName("agreedTermsIds")
    val agreedTermsIds: List<String>,
)
