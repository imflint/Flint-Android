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
                    name = "추리",
                    color = "BLUE",
                    rank = 1
                ),
                KeywordItemModel(
                    name = "슬픈",
                    color = "GREEN",
                    rank = 4
                ),
                KeywordItemModel(
                    name = "SF",
                    color = "PINK",
                    rank = 2,
                ),
                KeywordItemModel(
                    name = "액션",
                    color = "ORANGE",
                    rank = 5,
                ),
                KeywordItemModel(
                    name = "슬픈",
                    color = "YELLOW",
                    rank = 3,
                ),
                KeywordItemModel(
                    name = "성장",
                    color = "YELLOW",
                    rank = 6,
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
            runCatching { PreferenceType.valueOf(color) }
                .getOrDefault(PreferenceType.BLUE)
}
