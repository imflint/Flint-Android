package com.flint.presentation.collectioncreate

import androidx.compose.runtime.Immutable
import com.flint.presentation.collectioncreate.model.CollectionContentUiModel

@Immutable
data class CollectionCreateUiState(
    val title: String = "",
    val description: String = "",
    val isPublic: Boolean? = null,
    val selectedContents: MutableList<CollectionContentUiModel> = mutableListOf(),
){
    val isFinishButtonEnabled: Boolean
        =
        title.isNotEmpty() &&
        isPublic != null
//                &&
//        selectedContents.size >= 2
}