package com.flint.android.data.dto.exploration.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExplorationResponseDto(
    @SerialName("items") val items: List<Item>,
    // IN_PROGRESS / END / EMPTY
    @SerialName("state") val state: String,
    // 마지막으로 확인한 세션 내 위치. 시작 전은 0
    @SerialName("lastViewedPosition") val lastViewedPosition: Int,
) {
    @Serializable
    data class Item(
        @SerialName("contentId") val contentId: String,
        @SerialName("title") val title: String,
        @SerialName("description") val description: String = "",
        @SerialName("imageUrl") val imageUrl: String = "",
        @SerialName("year") val year: Int? = null,
        @SerialName("collectionId") val collectionId: String = "",
        // 세션 내 고정 위치(1~30). 숨김·삭제된 작품은 빠지므로 인덱스+1 과 다를 수 있다.
        @SerialName("position") val position: Int,
    )
}
