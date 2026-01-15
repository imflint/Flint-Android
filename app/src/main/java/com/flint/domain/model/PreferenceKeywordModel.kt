package com.flint.domain.model

import com.flint.domain.model.PreferenceKeywordModel.Companion.FakeList1
import com.flint.domain.type.PreferenceType
import kotlinx.collections.immutable.persistentListOf

data class PreferenceKeywordModel(
    val id: Long,
    val title: String,
    val imageUrl: String?,
    val level: PreferenceType,
    val rank: Int = 0,
    val percentage: Int = 0,
) {
    companion object {
        val FakeList1 =
            persistentListOf(
                PreferenceKeywordModel(
                    id = 1L,
                    title = "영화",
                    imageUrl = null,
                    level = PreferenceType.Blue,
                    rank = 1,
                    percentage = 90,
                ),
                PreferenceKeywordModel(
                    id = 2L,
                    title = "슬픈",
                    imageUrl = null,
                    level = PreferenceType.Green,
                    rank = 2,
                    percentage = 75,
                ),
                PreferenceKeywordModel(
                    id = 3L,
                    title = "SF",
                    imageUrl = null,
                    level = PreferenceType.Pink,
                    rank = 3,
                ),
                PreferenceKeywordModel(
                    id = 4L,
                    title = "액션",
                    imageUrl = null,
                    level = PreferenceType.Green,
                    rank = 4,
                ),
                PreferenceKeywordModel(
                    id = 5L,
                    title = "시리즈",
                    imageUrl = null,
                    level = PreferenceType.Yellow,
                    rank = 5,
                ),
                PreferenceKeywordModel(
                    id = 6L,
                    title = "성장",
                    imageUrl = null,
                    level = PreferenceType.Yellow,
                    rank = 6,
                ),
            )
        val FakeList2 =
            persistentListOf(
                FakeList1[0].copy(title = "시리즈"),
                FakeList1[1].copy(title = "애니메이션"),
                FakeList1[2].copy(title = "몽환적인"),
                FakeList1[3].copy(title = "다큐멘터리"),
                FakeList1[4].copy(title = "슬픈"),
                FakeList1[5].copy(title = "성장"),
            )
    }
}
