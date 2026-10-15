package com.flint.android.data.dto.exploration.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExplorationProgressRequestDto(
    // 세션 내 위치(1~30). 응답 item 의 position 값을 그대로 보낸다.
    @SerialName("lastViewedPosition")
    val lastViewedPosition: Int,
)
