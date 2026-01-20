package com.flint.presentation.collectioncreate

import androidx.compose.runtime.Immutable

@Immutable
data class CollectionCreateUiState(
    val title: String = "",
    val description: String = "",
    val isPublic: Boolean? = null,
    val selectedContents: MutableList<CollectionContentUiModel> = mutableListOf(),
)