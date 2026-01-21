package com.flint.presentation.collectioncreate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flint.R
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintIconButton
import com.flint.core.designsystem.component.button.FlintLargeButton
import com.flint.core.designsystem.component.textfield.FlintLongTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.collectioncreate.component.CollectionCreateContentDeleteModal
import com.flint.presentation.collectioncreate.component.CollectionCreateContentItemList
import com.flint.presentation.collectioncreate.component.CollectionCreateThumbnail
import com.flint.presentation.collectioncreate.model.CollectionContentUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CollectionCreateRoute(
    paddingValues: PaddingValues,
    navigateToAddContent: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: CollectionCreateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()


    CollectionCreateScreen(
        uiState = uiState,
        onTitleChanged = viewModel::updateTitle,
        onDescriptionChanged = viewModel::updateDescription,
        onPublicChanged = viewModel::updateIsPublic,
        selectedContents = uiState.selectedContents,
        onRemoveContent = viewModel::removeContent,
        onBackClick = navigateUp,
        onAddContentClick = navigateToAddContent,
        onFinishClick = viewModel::onClickFinish,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun CollectionCreateScreen(
    uiState: CollectionCreateUiState,
    onTitleChanged: (String) -> Unit = {},
    onDescriptionChanged: (String) -> Unit = {},
    onPublicChanged: (Boolean?) -> Unit = {},
    selectedContents: ImmutableList<CollectionContentUiModel>,
    onRemoveContent: (CollectionContentUiModel) -> Unit,
    onBackClick: () -> Unit,
    onAddContentClick: () -> Unit,
    onFinishClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isModalVisible by remember { mutableStateOf(false) }
    var contentToDelete by remember { mutableStateOf<CollectionContentUiModel?>(null) }



    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = FlintTheme.colors.background)
    ) {
        FlintBackTopAppbar(onClick = onBackClick)

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            // 썸네일
            item {
                CollectionCreateThumbnail(
                    imageUrl = "",
                    onClick = { },
                )

                Spacer(Modifier.height(8.dp))
            }

            // 컬렉션 제목
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "컬렉션 제목",
                        color = FlintTheme.colors.white,
                        style = FlintTheme.typography.head3M18,
                    )

                    Spacer(Modifier.height(16.dp))

                    FlintLongTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.title,
                        placeholder = "컬렉션의 제목을 입력해주세요.",
                        onValueChanged = onTitleChanged,
                        maxLength = 20,
                        height = 40.dp,
                    )
                }
            }

            // 컬렉션 소개
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text =
                            buildAnnotatedString {
                                append("컬렉션 소개 ")
                                withStyle(
                                    style = SpanStyle(color = FlintTheme.colors.gray300),
                                ) {
                                    append("(선택)")
                                }
                            },
                        color = FlintTheme.colors.white,
                        style = FlintTheme.typography.head3M18,
                    )

                    Spacer(Modifier.height(16.dp))

                    FlintLongTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.description,
                        placeholder = "컬렉션의 소개를 작성해주세요.",
                        onValueChanged = onDescriptionChanged,
                        maxLength = 200,
                        height = 104.dp,
                    )
                }
            }

            // 컬렉션 공개 여부
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "컬렉션 공개 여부",
                        color = FlintTheme.colors.white,
                        style = FlintTheme.typography.head3M18,
                    )

                    Spacer(Modifier.height(16.dp))

                    Row {
                        FlintIconButton(
                            text = "공개",
                            iconRes = R.drawable.ic_share,
                            state = when(uiState.isPublic){
                                true -> FlintButtonState.ColorOutline
                                false -> FlintButtonState.Disable
                                else -> FlintButtonState.Outline
                            },

                            onClick = { onPublicChanged(true) },
                            modifier = Modifier.weight(1f),
                        )

                        Spacer(Modifier.width(8.dp))

                        FlintIconButton(
                            text = "비공개",
                            iconRes = R.drawable.ic_lock,
                            state = when(uiState.isPublic){
                                true -> FlintButtonState.Disable
                                false -> FlintButtonState.ColorOutline
                                else -> FlintButtonState.Outline
                            },
                            onClick = { onPublicChanged(false) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            // 작품 추가 헤더
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "작품 추가",
                        color = FlintTheme.colors.white,
                        style = FlintTheme.typography.head3M18,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "최대 10개까지 추가할 수 있어요",
                            color = FlintTheme.colors.gray200,
                            style = FlintTheme.typography.body2R14,
                        )
                        Text(
                            text = "${selectedContents.size}/10",
                            color = FlintTheme.colors.white,
                            style = FlintTheme.typography.body2R14,
                        )
                    }
                }
            }

            // 작품 리스트
            items(
                items = selectedContents,
                key = { it.contentId },
            ) { content ->
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    CollectionCreateContentItemList(
                        onCancelClick = {
                            contentToDelete = content
                            isModalVisible = true
                        },
                        imageUrl = content.imageUrl,
                        title = content.title,
                        director = content.director,
                        createdYear = content.createdYear,
                    )
                }
            }

            // 작품 추가 버튼
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    FlintIconButton(
                        text = "작품 추가하기",
                        iconRes = R.drawable.ic_plus,
                        state = FlintButtonState.ColorOutline,
                        onClick = onAddContentClick,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 80.dp),
                    )

                    Spacer(Modifier.height(36.dp))
                }
            }
        }
        FlintLargeButton(
            text = "완료",
            state = if (uiState.isFinishButtonEnabled) FlintButtonState.Able else FlintButtonState.Disable,
            onClick = {onFinishClick()},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            enabled = uiState.isFinishButtonEnabled,
        )
    }


    if (isModalVisible) {
        CollectionCreateContentDeleteModal(
            onConfirm = {
                contentToDelete?.let { onRemoveContent(it) }
                contentToDelete = null
                isModalVisible = false
            },
            onDismiss = {
                contentToDelete = null
                isModalVisible = false
            },
        )
    }
}

@Preview
@Composable
fun CollectionCreateScreenPreview() {
    FlintTheme {
        CollectionCreateScreen(
            uiState = CollectionCreateUiState(),
            onTitleChanged = {},
            onDescriptionChanged = {},
            onPublicChanged = {},
            selectedContents = CollectionContentUiModel.dummyContentList,
            onRemoveContent = {},
            onBackClick = {},
            onAddContentClick = {},
            onFinishClick = {},
        )
    }
}
