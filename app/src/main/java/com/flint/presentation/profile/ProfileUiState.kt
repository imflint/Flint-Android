package com.flint.presentation.profile

import androidx.compose.runtime.Immutable
import com.flint.data.model.user.UserKeywordListModel
import com.flint.data.model.user.UserKeywordModel
import com.flint.data.model.user.AuthorModel
import com.flint.data.model.collection.CollectionModel
import com.flint.data.model.content.ContentModel
import com.flint.domain.type.PreferenceType
import com.flint.domain.type.UserRoleType
import com.flint.presentation.profile.model.UserKeywordUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class ProfileUiState(
    val keywords: ImmutableList<UserKeywordUiModel>,
    // TODO: UiModel로 변경
    val profile: AuthorModel,
    val savedContent: ImmutableList<ContentModel>,
    val createCollections: ImmutableList<CollectionModel>,
    val savedCollections: ImmutableList<CollectionModel>,
) {
    companion object {
        val Empty = ProfileUiState(
            keywords = persistentListOf(),
            profile = AuthorModel(
                userId = 800370427074376600, //TODO: 임시 userId
                nickname = "",
                profileUrl = "",
                userRole = UserRoleType.FLINER,
            ),
            createCollections = persistentListOf(),
            savedCollections = persistentListOf(),
            savedContent = persistentListOf(),
        )
        val Fake = ProfileUiState(
            keywords = UserKeywordUiModel.FakeList1,
            profile = AuthorModel.Fake,
            createCollections = CollectionModel.FakeList,
            savedCollections = CollectionModel.FakeList,
            savedContent = ContentModel.FakeList,
        )
    }
 }

fun UserKeywordListModel.toState(): ImmutableList<UserKeywordUiModel> = this.keywordList.map { it.toState() }.toImmutableList()

private fun UserKeywordModel.toState(): UserKeywordUiModel =
    UserKeywordUiModel(
        name = this.name,
        imageUrl = this.imageUrl,
        preferenceType = PreferenceType.valueOf(this.color),
        rank = this.rank,
        percentage = this.percentage
    )