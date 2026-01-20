package com.flint.presentation.profile.uistate

import androidx.compose.runtime.Immutable
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.content.BookmarkedContentListModel
import com.flint.domain.model.user.UserKeywordResponseModel
import com.flint.domain.model.user.UserProfileResponseModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ProfileUiState(
    val keywords: ImmutableList<UserKeywordResponseModel>,
    val profile: UserProfileResponseModel,
    val savedContent: BookmarkedContentListModel,
    val createCollections: CollectionListModel,
    val savedCollections: CollectionListModel,
) {
    companion object {
        val Empty =
            ProfileUiState(
                keywords = persistentListOf(),
                profile = UserProfileResponseModel.Companion.Empty,
                createCollections = CollectionListModel.FakeList,
                savedCollections = CollectionListModel.FakeList,
                savedContent = BookmarkedContentListModel.FakeList,
            )
        val Fake =
            ProfileUiState(
                keywords = UserKeywordResponseModel.Companion.FakeList1,
                profile = UserProfileResponseModel.Companion.Fake,
                createCollections = CollectionListModel.FakeList,
                savedCollections = CollectionListModel.FakeList,
                savedContent = BookmarkedContentListModel.FakeList,
            )
    }
}