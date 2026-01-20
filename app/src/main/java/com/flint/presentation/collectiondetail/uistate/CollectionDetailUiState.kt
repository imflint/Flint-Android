package com.flint.presentation.collectiondetail.uistate

import com.flint.domain.model.bookmark.CollectionBookmarkUsersModel
import com.flint.domain.model.collection.CollectionDetailModelNew

data class CollectionDetailUiState(
    val collectionDetail: CollectionDetailModelNew,
    val collectionBookmarkUsers: CollectionBookmarkUsersModel,
)