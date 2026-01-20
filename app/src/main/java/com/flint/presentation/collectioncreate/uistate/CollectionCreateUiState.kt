package com.flint.presentation.collectioncreate.uistate

import androidx.compose.runtime.Immutable
import com.flint.presentation.collectioncreate.CollectionContentUiModel

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