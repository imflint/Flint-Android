package com.flint.presentation.collectioncreate

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.flint.domain.model.search.SearchContentItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CollectionCreateUiState(
    val thumbnailImageUri: Uri? = null,
    val title: String = "",
    val description: String = "",
    val isPublic: Boolean? = null,
    val selectedContents: ImmutableList<SearchContentItemModel> = persistentListOf(),
    val contentDetailsMap: Map<String, ContentDetail> = emptyMap(),
    val contents: ImmutableList<SearchContentItemModel> = persistentListOf(),
    val searchText: String = "",
    val isLoading: Boolean = false,
) {
    val isFinishButtonEnabled: Boolean =
        !isLoading &&
        title.isNotBlank() &&
        isPublic != null &&
        selectedContents.size >= 2 &&
        selectedContents.all { contentDetailsMap[it.id]?.reason?.isNotBlank() == true }

    val isCancelModalVisible: Boolean =
        contentDetailsMap.values.any { it.reason.isNotBlank() }
}

@Immutable
data class ContentDetail(
    val isSpoiler: Boolean = false,
    val reason: String = "",
    val contentImageUris: List<Uri> = emptyList(),
)