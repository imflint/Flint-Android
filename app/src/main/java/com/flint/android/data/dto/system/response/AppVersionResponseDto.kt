package com.flint.android.data.dto.system.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppVersionResponseDto(
    // 최소 지원 버전 (x.y.z)
    @SerialName("version")
    val version: String,
    // 스토어 이동 주소
    @SerialName("path")
    val path: String = "",
)
