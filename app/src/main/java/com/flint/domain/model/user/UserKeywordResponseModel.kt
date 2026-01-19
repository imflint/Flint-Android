package com.flint.domain.model.user

import com.flint.domain.type.PreferenceType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UserKeywordResponseModel(
    val color: String,
    val rank: Int,
    val name: String,
    val percentage: Float,
    val imageUrl: String,
) {
    val preferenceType: PreferenceType
        get() =
            runCatching { PreferenceType.valueOf(color) }
                .getOrDefault(PreferenceType.BLUE)

    companion object {
        val FakeList1: ImmutableList<UserKeywordResponseModel> =
            persistentListOf(
                UserKeywordResponseModel(
                    name = "추리",
                    imageUrl = "",
                    color = "BLUE",
                    rank = 1,
                    percentage = 90f,
                ),
                UserKeywordResponseModel(
                    name = "슬픈",
                    imageUrl = "",
                    color = "GREEN",
                    rank = 4,
                    percentage = 75f,
                ),
                UserKeywordResponseModel(
                    name = "SF",
                    imageUrl = "",
                    color = "PINK",
                    rank = 2,
                    percentage = 7f,
                ),
                UserKeywordResponseModel(
                    name = "액션",
                    imageUrl = "",
                    color = "GREEN",
                    rank = 5,
                    percentage = 0f,
                ),
                UserKeywordResponseModel(
                    name = "슬픈",
                    imageUrl = "",
                    color = "YELLOW",
                    rank = 3,
                    percentage = 0f,
                ),
                UserKeywordResponseModel(
                    name = "성장",
                    imageUrl = "",
                    color = "YELLOW",
                    rank = 6,
                    percentage = 0f,
                ),
            )

        val FakeList2: ImmutableList<UserKeywordResponseModel> =
            persistentListOf(
                FakeList1[0],
                FakeList1[1],
                FakeList1[2],
                FakeList1[3].copy(name = "키워드"),
                FakeList1[4].copy(name = "설레는"),
                FakeList1[5].copy(name = "키워드"),
            )

        val FakeList3: ImmutableList<UserKeywordResponseModel> =
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
