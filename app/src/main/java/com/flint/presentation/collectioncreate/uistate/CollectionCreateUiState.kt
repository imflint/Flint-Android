package com.flint.presentation.collectioncreate

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.flint.presentation.collectioncreate.model.CollectionContentUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CollectionCreateUiState(
    val title: String = "",
    val description: String = "",
    val isPublic: Boolean? = null,
    val selectedContents: ImmutableList<CollectionContentUiModel> = persistentListOf(),

    val searchText: String = "",
    val isSpoiler: Boolean = false,
    val selectedReason: String = "",
){
    val isFinishButtonEnabled: Boolean
            =
        title.isNotEmpty() &&
        isPublic != null &&
        selectedContents.size >= 2
}