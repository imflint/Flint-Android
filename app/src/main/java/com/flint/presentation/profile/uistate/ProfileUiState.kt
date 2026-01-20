package com.flint.presentation.profile.uistate

import androidx.compose.runtime.Immutable
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.content.BookmarkedContentListModel
import com.flint.domain.model.user.KeywordListModel
import com.flint.domain.model.user.UserProfileResponseModel

@Immutable
data class ProfileUiState(
    val keywords: KeywordListModel,
    val profile: UserProfileResponseModel,
    val savedContent: BookmarkedContentListModel,
    val createCollections: CollectionListModel,
    val savedCollections: CollectionListModel,
) {
    companion object {
        val Empty =
            ProfileUiState(
                keywords = KeywordListModel(),
                profile = UserProfileResponseModel.Companion.Empty,
                createCollections = CollectionListModel.FakeList,
                savedCollections = CollectionListModel.FakeList,
                savedContent = BookmarkedContentListModel.FakeList,
            )
        val Fake =
            ProfileUiState(
                keywords = KeywordListModel.FakeList1,
                profile = UserProfileResponseModel.Companion.Fake,
                createCollections = CollectionListModel.FakeList,
                savedCollections = CollectionListModel.FakeList,
                savedContent = BookmarkedContentListModel.FakeList,
            )
    }
}