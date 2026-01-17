package com.flint.presentation.profile.model

import com.flint.core.common.type.PreferenceType
import kotlinx.collections.immutable.persistentListOf

data class UserKeywordUiModel(
    val name: String,
    val imageUrl: String?,
    val preferenceType: PreferenceType,
    val rank: Int = 0,
    val percentage: Float = 0f,
) {
    companion object {
        val FakeList1 =
            persistentListOf(
                UserKeywordUiModel(
                    name = "영화",
                    imageUrl = null,
                    preferenceType = PreferenceType.BLUE,
                    rank = 1,
                    percentage = 90f,
                ),
                UserKeywordUiModel(
                    name = "슬픈",
                    imageUrl = null,
                    preferenceType = PreferenceType.GREEN,
                    rank = 2,
                    percentage = 75f,
                ),
                UserKeywordUiModel(
                    name = "SF",
                    imageUrl = null,
                    preferenceType = PreferenceType.PINK,
                    rank = 3,
                    percentage = 7f,
                ),
                UserKeywordUiModel(
                    name = "액션",
                    imageUrl = null,
                    preferenceType = PreferenceType.GREEN,
                    rank = 4,
                ),
                UserKeywordUiModel(
                    name = "시리즈",
                    imageUrl = null,
                    preferenceType = PreferenceType.YELLOW,
                    rank = 5,
                ),
                UserKeywordUiModel(
                    name = "성장",
                    imageUrl = null,
                    preferenceType = PreferenceType.YELLOW,
                    rank = 6,
                ),
            )
        val FakeList2 =
            persistentListOf(
                FakeList1[0].copy(name = "시리즈"),
                FakeList1[1].copy(name = "애니메이션"),
                FakeList1[2].copy(name = "몽환적인"),
                FakeList1[3].copy(name = "다큐멘터리"),
                FakeList1[4].copy(name = "슬픈"),
                FakeList1[5].copy(name = "성장"),
            )
    }
}
