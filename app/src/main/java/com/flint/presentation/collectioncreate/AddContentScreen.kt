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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flint.core.designsystem.component.image.SelectedContentItem
import com.flint.core.designsystem.component.textfield.FlintSearchTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.collectioncreate.component.CollectionCreateContentSelect
import com.flint.presentation.collectioncreate.model.CollectionContentUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun AddContentRoute(
    paddingValues: PaddingValues,
    navigateToCollectionCreate: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: CollectionCreateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val contentList =
        remember {
            CollectionContentUiModel.dummyContentList.toMutableStateList()
        }

    AddContentScreen(
        uiState = uiState,
        contentList = contentList.toImmutableList(),
        selectedContents = uiState.selectedContents,
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
    contentList: ImmutableList<CollectionContentUiModel>,
    selectedContents: ImmutableList<CollectionContentUiModel>,
    onSearchTextChanged: (String) -> Unit = {},
    onToggleContent: (CollectionContentUiModel) -> Unit = {},
    onBackClick: () -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = FlintTheme.colors.background),
    ) {
        FlintBackTopAppbar(
            onClick = onBackClick,
            title = "작품 추가하기",
            actionText = "추가",
            onActionClick = if (selectedContents.isNotEmpty()) onActionClick else {{}},
            textStyle = if (selectedContents.isNotEmpty()) FlintTheme.typography.body1M16 else FlintTheme.typography.body1Sb16,
            textColor = if (selectedContents.isNotEmpty()) FlintTheme.colors.secondary400 else FlintTheme.colors.gray300,
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlintSearchTextField(
            placeholder = "추천하고 싶은 작품을 검색해보세요",
            value = uiState.searchText,
            onValueChanged = onSearchTextChanged,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedContents.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    items = selectedContents,
                    key = { it.contentId },
                ) { content ->
                    SelectedContentItem(
                        imageUrl = content.imageUrl,
                        onRemoveClick = {
                            onToggleContent(content)
                        },
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
            // 작품 리스트
            items(
                items = contentList,
                key = { it.contentId },
            ) { content ->
                val isSelected = selectedContents.contains(content)

                CollectionCreateContentSelect(
                    onCheckClick = {
                        onToggleContent(content)
                    },
                    isSelected = isSelected,
                    imageUrl = content.imageUrl,
                    title = content.title,
                    director = content.director,
                    createdYear = content.createdYear,
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
            selectedContents = CollectionContentUiModel.dummyContentList,
            onSearchTextChanged = {},
            onBackClick = {},
            onActionClick = {},
            contentList = CollectionContentUiModel.dummyContentList,
        )
    }
}
