package com.flint.data.dto.ott.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OttListResponseDto(
    @SerialName("otts")
    val otts: List<OttItemResponseDto>,
)

@Serializable
data class OttItemResponseDto(
    @SerialName("ottId")
    val ottId: String,
    @SerialName("name")
    val name: String,
    @SerialName("logoUrl")
    val logoUrl: String,
    // 서버 응답(GetOttResponse)에 없는 필드. 기본값이 없으면 역직렬화가 실패한다
    @SerialName("contentUrl")
    val contentUrl: String = "",
)