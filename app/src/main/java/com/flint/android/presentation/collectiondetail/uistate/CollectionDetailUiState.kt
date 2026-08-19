package com.flint.android.presentation.collectiondetail.uistate

import com.flint.android.domain.model.bookmark.CollectionBookmarkUsersModel
import com.flint.android.domain.model.collection.CollectionDetailModelNew

data class CollectionDetailUiState(
    val collectionDetail: CollectionDetailModelNew,
    val collectionBookmarkUsers: CollectionBookmarkUsersModel,
    val isMine: Boolean,
)