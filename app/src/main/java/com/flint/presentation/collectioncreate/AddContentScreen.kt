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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flint.core.designsystem.component.image.SelectedContentItem
import com.flint.core.designsystem.component.textfield.FlintSearchTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.collectioncreate.component.CollectionCreateContentSelect

@Composable
fun AddContentRoute(
    paddingValues: PaddingValues,
    navigateToCollectionCreate: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: CollectionCreateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    AddContentScreen(
        uistate = uiState,
        onTitleChanged = viewModel::updateTitle,
        onBackClick = navigateUp,
        onActionClick = navigateToCollectionCreate,
        modifier = Modifier.padding(paddingValues),
    )
}

data class CollectionContentUiModel(
    val contentId: Long,
    val imageUrl: String,
    val title: String,
    val director: String,
    val createdYear: String,
)

@Composable
fun AddContentScreen(
    uistate: CollectionCreateUiState,
    onTitleChanged: (String) -> Unit = {},
    onBackClick: () -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var selectedContents = remember { mutableStateListOf<CollectionContentUiModel>() }

    val contentList =
        remember {
            mutableStateListOf(
                CollectionContentUiModel(1L, "https://buly.kr/DEaVFRZ", "해리포터 불의 잔", "마이크 뉴웰", "2005"),
                CollectionContentUiModel(2L, "https://buly.kr/2UkIDen", "인터스텔라", "크리스토퍼 놀란", "2014"),
                CollectionContentUiModel(3L, "https://buly.kr/FAeqqRB", "라라랜드", "데이미언 셔젤", "2016"),
                CollectionContentUiModel(4L, "https://buly.kr/DPVH2Ob", "라라랜드", "데이미언 셔젤", "2016"),
                CollectionContentUiModel(5L, "https://buly.kr/DEaVFRZ", "라라랜드", "데이미언 셔젤", "2016"),
                CollectionContentUiModel(6L, "https://buly.kr/DEaVFRZ", "라라랜드", "데이미언 셔젤", "2016"),
                CollectionContentUiModel(7L, "https://buly.kr/DEaVFRZ", "라라랜드", "데이미언 셔젤", "2016"),
                CollectionContentUiModel(8L, "https://buly.kr/DEaVFRZ", "라라랜드", "데이미언 셔젤", "2016"),
            )
        }

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
            onActionClick = onActionClick,
            textColor = FlintTheme.colors.gray300,
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlintSearchTextField(
            placeholder = "추천하고 싶은 작품을 검색해보세요",
            value = searchText,
            onValueChanged = { searchText = it },
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
                ) { content: CollectionContentUiModel ->
                    SelectedContentItem(
                        imageUrl = content.imageUrl,
                        onRemoveClick = {
                            selectedContents.remove(content)
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
                        if (isSelected) {
                            selectedContents.remove(content)
                        } else {
                            selectedContents.add(content)
                        }
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
            onBackClick = {},
            onActionClick = {},
            uistate = CollectionCreateUiState()
        )
    }
}
