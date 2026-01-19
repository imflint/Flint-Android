package com.flint.domain.model.collection

import com.flint.domain.model.user.AuthorModel
import kotlinx.collections.immutable.persistentListOf

data class CollectionDetailModel(
    val collectionId: String,
    val collectionTitle: String,
    val collectionContent: String,
    val collectionImageUrl1: String,
    val collectionImageUrl2: String,
    val createdAt: String,
    val isBookmarked: Boolean,
    val bookmarkCount: Int,
    val author: AuthorModel,
) {
    companion object {
        val Fake =
            CollectionDetailModel(
                collectionId = "id_0",
                collectionTitle = "컬렉션 제목 1",
                collectionContent = "컬렉션에 대한 설명이 들어갑니다. 최대 2줄까지 표시됩니다.",
                collectionImageUrl1 = "",
                collectionImageUrl2 = "",
                createdAt = "2024-01-01",
                isBookmarked = true,
                bookmarkCount = 10,
                author = AuthorModel.Companion.Fake,
            )
        
        val FakeList = persistentListOf(
            Fake.copy(
                collectionId = "1",
                collectionTitle = "2024 인생 영화 모음",
                collectionContent = "올해 본 영화 중 최고만 모았습니다",
                isBookmarked = true,
                bookmarkCount = 128,
                author = AuthorModel.Fake.copy(nickname = "닉네임은여덟글자"),
            ),
            Fake.copy(
                collectionId = "2",
                collectionTitle = "혼자 보기 아까운 숨은 명작",
                collectionContent = "알려지지 않은 갓작들",
                isBookmarked = false,
                bookmarkCount = 56,
                author = AuthorModel.Fake.copy(nickname = "시네필"),
            ),
            Fake.copy(
                collectionId = "3",
                collectionTitle = "비 오는 날 보기 좋은 영화",
                collectionContent = "감성 충전용 영화 컬렉션입니다. 우울할 때 보면 좋아요.",
                isBookmarked = true,
                bookmarkCount = 234,
                author = AuthorModel.Fake.copy(nickname = "무비러버"),
            ),
            Fake.copy(
                collectionId = "4",
                collectionTitle = "넷플릭스 정주행 리스트",
                collectionContent = "주말에 몰아보기 좋은 시리즈",
                isBookmarked = false,
                bookmarkCount = 89,
                author = AuthorModel.Fake,
            ),
            Fake.copy(
                collectionId = "5",
                collectionTitle = "SF 덕후 추천작",
                collectionContent = "우주와 미래를 다룬 SF 명작 모음",
                isBookmarked = true,
                bookmarkCount = 412,
                author = AuthorModel.Fake.copy(nickname = "SF매니아"),
            ),
            Fake.copy(
                collectionId = "6",
                collectionTitle = "첫 데이트용 무난한 영화",
                collectionContent = "어색한 분위기를 풀어줄 영화들",
                isBookmarked = false,
                bookmarkCount = 67,
                author = AuthorModel.Fake.copy(nickname = "연애고수"),
            ),
        )
    }
}