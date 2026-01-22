package com.flint.presentation.collectioncreate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flint.core.designsystem.component.image.SelectedContentItem
import com.flint.core.designsystem.component.textfield.FlintSearchTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.search.SearchContentItemModel
import com.flint.domain.model.search.SearchContentListModel
import com.flint.presentation.collectioncreate.component.CollectionCreateContentSelect
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AddContentRoute(
    paddingValues: PaddingValues,
    navigateToCollectionCreate: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: CollectionCreateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    AddContentScreen(
        uiState = uiState,
        selectedContents = uiState.selectedContents,
        contentList = uiState.contents,
        onSearchTextChanged = viewModel::updateSearch,
        onToggleContent = viewModel::toggleContent,
        onBackClick = navigateUp,
        onActionClick = navigateToCollectionCreate,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun AddContentScreen(
    uiState: CollectionCreateUiState,
    selectedContents: ImmutableList<SearchContentItemModel>,
    contentList: ImmutableList<SearchContentItemModel>,
    onSearchTextChanged: (String) -> Unit = {},
    onToggleContent: (SearchContentItemModel) -> Unit = {},
    onBackClick: () -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val lazyRowState = rememberLazyListState()

    LaunchedEffect(selectedContents.size) {
        if (selectedContents.isNotEmpty()) {
            lazyRowState.animateScrollToItem(selectedContents.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = FlintTheme.colors.background),
    ) {
        FlintBackTopAppbar(
            onClick = onBackClick,
            title = "작품 추가하기",
            actionText = "추가",
            onActionClick = {
                if (selectedContents.isNotEmpty()) onActionClick()
            },
            textStyle = if (selectedContents.isNotEmpty()) FlintTheme.typography.body1M16 else FlintTheme.typography.body1Sb16,
            textColor = if (selectedContents.isNotEmpty()) FlintTheme.colors.secondary400 else FlintTheme.colors.gray300,
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlintSearchTextField(
            placeholder = "추천하고 싶은 작품을 검색해보세요",
            value = uiState.searchText,
            onValueChanged = onSearchTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedContents.isNotEmpty()) {
            LazyRow(
                state = lazyRowState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true,
            ) {
                items(
                    items = selectedContents,
                    key = { it.id },
                ) { content ->
                    SelectedContentItem(
                        imageUrl = content.posterUrl,
                        onRemoveClick = { onToggleContent(content) },
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = contentList,
                key = { it.id },
            ) { content ->
                val isSelected = selectedContents.any { it.id == content.id }

                CollectionCreateContentSelect(
                    onCheckClick = { onToggleContent(content) },
                    isSelected = isSelected,
                    imageUrl = content.posterUrl,
                    title = content.title,
                    director = content.author,
                    createdYear = content.year,
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddContentScreenPreview() {
    FlintTheme {
        AddContentScreen(
            uiState = CollectionCreateUiState(),
            contentList = SearchContentListModel.FakeList,
            selectedContents = SearchContentListModel.FakeList,
            onSearchTextChanged = {},
            onToggleContent = {},
            onBackClick = {},
            onActionClick = {},
        )
    }
}
