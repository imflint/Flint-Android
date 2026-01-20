package com.flint.presentation.profile

import androidx.compose.runtime.Immutable
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.collection.CollectionModel
import com.flint.domain.model.content.BookmarkedContentListModel
import com.flint.domain.model.content.ContentModel
import com.flint.domain.model.user.AuthorModel
import com.flint.domain.model.user.UserKeywordResponseModel
import com.flint.domain.type.UserRoleType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ProfileUiState(
    val keywords: ImmutableList<UserKeywordResponseModel>,
    val profile: AuthorModel,
    val savedContent: BookmarkedContentListModel,
    val createCollections: CollectionListModel,
    val savedCollections: CollectionListModel,
) {
    companion object {
        val Empty =
            ProfileUiState(
                keywords = persistentListOf(),
                profile =
                    AuthorModel(
                        userId = "0",
                        nickname = "",
                        profileUrl = "",
                        userRole = UserRoleType.FLINER,
                    ),
                createCollections = CollectionListModel.FakeList,
                savedCollections = CollectionListModel.FakeList,
                savedContent = BookmarkedContentListModel.FakeList,
            )
        val Fake =
            ProfileUiState(
                keywords = UserKeywordResponseModel.FakeList1,
                profile = AuthorModel.Fake,
                createCollections = CollectionListModel.FakeList,
                savedCollections = CollectionListModel.FakeList,
                savedContent = BookmarkedContentListModel.FakeList,
            )
    }
}
