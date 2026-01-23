package com.flint.presentation.profile.uistate

import androidx.compose.runtime.Immutable
import com.flint.core.common.util.UiState
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.content.BookmarkedContentListModel
import com.flint.domain.model.user.KeywordListModel
import com.flint.domain.model.user.UserProfileResponseModel

@Immutable
data class ProfileUiState(
    val userId: String? = null,
    val profile: UserProfileResponseModel = UserProfileResponseModel.Empty,
    val sectionData: UiState<ProfileSectionData> = UiState.Loading,
) {
    companion object {
        val Empty =
            ProfileUiState(
                profile = UserProfileResponseModel.Empty,
                sectionData = UiState.Loading,
            )
        val Fake =
            ProfileUiState(
                profile = UserProfileResponseModel.Fake,
                sectionData = UiState.Success(ProfileSectionData.Fake),
            )
    }
}

@Immutable
data class ProfileSectionData(
    val keywords: KeywordListModel = KeywordListModel(),
    val createCollections: CollectionListModel = CollectionListModel(),
    val savedCollections: CollectionListModel = CollectionListModel(),
    val savedContents: BookmarkedContentListModel = BookmarkedContentListModel(),
) {
    companion object {
        val Fake = ProfileSectionData(
            keywords = KeywordListModel.FakeList1,
            createCollections = CollectionListModel.FakeList,
            savedCollections = CollectionListModel.FakeList,
            savedContents = BookmarkedContentListModel.FakeList,
        )
    }
}