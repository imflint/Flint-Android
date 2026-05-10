package com.flint.domain.model.user

import com.flint.domain.type.PreferenceType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class KeywordListModel(
    val keywords: ImmutableList<KeywordItemModel> = persistentListOf(),
) {
    companion object {
        val FakeList1 = KeywordListModel(
            keywords = persistentListOf(
                KeywordItemModel(
                    name = "애니메이션",
                    color = "BLUE",
                    rank = 1,
                    percentage = 75f,
                ),
                KeywordItemModel(
                    name = "슬픈",
                    color = "GREEN",
                    rank = 4,
                    percentage = 35f,
                ),
                KeywordItemModel(
                    name = "SF",
                    color = "PINK",
                    rank = 2,
                    percentage = 60f,
                ),
                KeywordItemModel(
                    name = "액션",
                    color = "ORANGE",
                    rank = 5,
                    percentage = 25f,
                ),
                KeywordItemModel(
                    name = "몽환적인",
                    color = "YELLOW",
                    rank = 3,
                    percentage = 48f,
                ),
                KeywordItemModel(
                    name = "성장",
                    color = "YELLOW",
                    rank = 6,
                    percentage = 15f,
                ),
            )
        )

        val FakeList2 = KeywordListModel(
            keywords = persistentListOf(
                FakeList1.keywords[0],
                FakeList1.keywords[1],
                FakeList1.keywords[2],
                FakeList1.keywords[3].copy(name = "키워드"),
                FakeList1.keywords[4].copy(name = "설레는"),
                FakeList1.keywords[5].copy(name = "키워드"),
            )
        )

        val FakeList3 = KeywordListModel(
            keywords = persistentListOf(
                FakeList1.keywords[0].copy(name = "시리즈"),
                FakeList1.keywords[1].copy(name = "애니메이션"),
                FakeList1.keywords[2].copy(name = "몽환적인"),
                FakeList1.keywords[3].copy(name = "다큐멘터리"),
                FakeList1.keywords[4].copy(name = "슬픈"),
                FakeList1.keywords[5].copy(name = "성장"),
            )
        )
    }
}

data class KeywordItemModel(
    val color: String = "",
    val rank: Int = 0,
    val name: String = "",
    val percentage: Float = 0f,
    val imageUrl: String = "",
) {
    val preferenceType: PreferenceType
        get() =
            runCatching { PreferenceType.valueOf(color.uppercase()) }
                .getOrDefault(PreferenceType.BLUE)
}
