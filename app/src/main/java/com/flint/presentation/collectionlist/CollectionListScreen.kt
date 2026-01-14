package com.flint.presentation.collectionlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

@Composable
fun CollectionListRoute(
    paddingValues: PaddingValues,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
) {
    CollectionListScreen()
}

@Composable
fun CollectionListScreen() {
}
