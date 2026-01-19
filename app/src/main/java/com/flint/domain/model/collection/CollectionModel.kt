package com.flint.domain.model.collection

import com.flint.domain.model.user.AuthorModel
import com.flint.domain.type.UserRoleType
import kotlinx.collections.immutable.persistentListOf

data class CollectionModel(
    val collectionId: String = "",
    val thumbnailUrl: String = "",
    val collectionTitle: String = "",
    val collectionImageUrl: String = "",
    val description: String = "",
    val imageList: List<String> = emptyList(),
    val bookmarkCount: Int = 0,
    val createdAt: String = "",
    val isBookmarked: Boolean = false,
    val author: AuthorModel = AuthorModel.Fake,
    val profileUrl: String = ""
) {
    companion object {
        val FakeList =
            persistentListOf(
                CollectionModel(
                    collectionId = "",
                    collectionTitle = "컬렉션 제목",
                    collectionImageUrl = "",
                    createdAt = "",
                    isBookmarked = false,
                    author =
                        AuthorModel(
                            userId = "0",
                            nickname = "사용자 이름",
                            profileUrl = "",
                            userRole = UserRoleType.FLINER,
                        ),
                ),
                CollectionModel(
                    collectionId = "",
                    collectionTitle = "컬렉션 제목2",
                    collectionImageUrl = "",
                    createdAt = "",
                    isBookmarked = false,
                    author =
                        AuthorModel(
                            userId = "0",
                            nickname = "사용자 이름2",
                            profileUrl = "",
                            userRole = UserRoleType.FLINER,
                        ),
                ),
            )
    }
}
