package com.flint.presentation.profile.uistate

import androidx.compose.runtime.Immutable
import com.flint.domain.model.collection.CollectionModel
import com.flint.domain.model.content.ContentModel
import com.flint.domain.model.user.UserKeywordResponseModel
import com.flint.domain.model.user.UserProfileResponseModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ProfileUiState(
    val keywords: ImmutableList<UserKeywordResponseModel>,
    val profile: UserProfileResponseModel,
    val savedContent: ImmutableList<ContentModel> = persistentListOf(),
    val createCollections: ImmutableList<CollectionModel> = persistentListOf(),
    val savedCollections: ImmutableList<CollectionModel> = persistentListOf(),
) {
    companion object {
        val Empty =
            ProfileUiState(
                keywords = persistentListOf(),
                profile = UserProfileResponseModel.Companion.Empty,
                createCollections = persistentListOf(),
                savedCollections = persistentListOf(),
                savedContent = persistentListOf(),
            )
        val Fake =
            ProfileUiState(
                keywords = UserKeywordResponseModel.Companion.FakeList1,
                profile = UserProfileResponseModel.Companion.Fake,
                createCollections = CollectionModel.Companion.FakeList,
                savedCollections = CollectionModel.Companion.FakeList,
                savedContent = ContentModel.Companion.FakeList,
            )
    }
}