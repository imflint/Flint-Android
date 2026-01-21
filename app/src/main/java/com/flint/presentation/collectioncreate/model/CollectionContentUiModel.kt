package com.flint.presentation.collectioncreate.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

//TODO: UiModel 삭제 필요
data class CollectionContentUiModel(
    val contentId: Int,
    val imageUrl: String,
    val title: String,
    val director: String,
    val createdYear: String,
)
    //: ImmutableList<CollectionContentUiModel>
{
    companion object {
        val dummyContentList =
            persistentListOf(
                CollectionContentUiModel(contentId = 1, imageUrl = "https://buly.kr/DEaVFRZ", title = "해리포터 불의 잔", director = "마이크 뉴웰", createdYear = "2005"),
                CollectionContentUiModel(contentId = 2, imageUrl = "https://buly.kr/2UkIDen", title = "인터스텔라", director = "크리스토퍼 놀란", createdYear = "2014"),
                CollectionContentUiModel(contentId = 3, imageUrl = "https://buly.kr/FAeqqRB", title = "라라랜드", director = "데이미언 셔젤", createdYear = "2016"),
                CollectionContentUiModel(contentId = 4, imageUrl = "https://buly.kr/DPVH2Ob", title = "라라랜드", director = "데이미언 셔젤", createdYear = "2016"),
                CollectionContentUiModel(contentId = 5, imageUrl = "https://buly.kr/DEaVFRZ", title = "라라랜드", director = "데이미언 셔젤", createdYear = "2016"),
                CollectionContentUiModel(contentId = 6, imageUrl = "https://buly.kr/DEaVFRZ", title = "라라랜드", director = "데이미언 셔젤", createdYear = "2016"),
                CollectionContentUiModel(contentId = 7, imageUrl = "https://buly.kr/DEaVFRZ", title = "라라랜드", director = "데이미언 셔젤", createdYear = "2016"),
                CollectionContentUiModel(contentId = 8, imageUrl = "https://buly.kr/DEaVFRZ", title = "라라랜드", director = "데이미언 셔젤", createdYear = "2016"),
            )
    }
}
