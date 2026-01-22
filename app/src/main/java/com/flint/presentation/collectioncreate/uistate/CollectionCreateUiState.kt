package com.flint.presentation.collectioncreate

import androidx.compose.runtime.Immutable
import com.flint.domain.model.search.SearchContentItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CollectionCreateUiState(
    val title: String = "",
    val description: String = "",
    val isPublic: Boolean? = null,
    val selectedContents: ImmutableList<SearchContentItemModel> = persistentListOf(),
    val contentDetailsMap: Map<String, ContentDetail> = emptyMap(),
    val contents: ImmutableList<SearchContentItemModel> = persistentListOf(),
    val searchText: String = "",

) {
    val isFinishButtonEnabled: Boolean =
        title.isNotBlank() &&
        isPublic != null &&
        selectedContents.size >= 2

    val isCancelModalVisible: Boolean =
        contentDetailsMap.values.any { it.reason.isNotBlank() }
}

@Immutable
data class ContentDetail(
    val isSpoiler: Boolean = false,
    val reason: String = ""
)