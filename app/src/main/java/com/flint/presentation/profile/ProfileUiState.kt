package com.flint.presentation.profile

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
    val savedContent: ImmutableList<ContentModel>,
    val createCollections: ImmutableList<CollectionModel>,
    val savedCollections: ImmutableList<CollectionModel>,
) {
    companion object {
        val Empty =
            ProfileUiState(
                keywords = persistentListOf(),
                profile = UserProfileResponseModel.Empty,
                createCollections = persistentListOf(),
                savedCollections = persistentListOf(),
                savedContent = persistentListOf(),
            )
        val Fake =
            ProfileUiState(
                keywords = UserKeywordResponseModel.FakeList1,
                profile = UserProfileResponseModel.Fake,
                createCollections = CollectionModel.FakeList,
                savedCollections = CollectionModel.FakeList,
                savedContent = ContentModel.FakeList,
            )
    }
}
