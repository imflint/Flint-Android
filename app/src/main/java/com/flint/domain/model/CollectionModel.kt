package com.flint.domain.model

import com.flint.domain.type.UserRoleType
import kotlinx.collections.immutable.persistentListOf

data class CollectionModel(
    val collectionId: String,
    val collectionTitle: String,
    val collectionImageUrl: String,
    val createdAt: String,
    val isBookmarked: Boolean,
    val author: AuthorModel,
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
                            userId = 0,
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
                            userId = 0,
                            nickname = "사용자 이름2",
                            profileUrl = "",
                            userRole = UserRoleType.FLINER,
                        ),
                ),
            )
    }
}
