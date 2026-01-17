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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.flint.R
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintIconButton
import com.flint.core.designsystem.component.button.FlintLargeButton
import com.flint.core.designsystem.component.textfield.FlintLongTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.collectioncreate.component.CollectionAddFilmBottomSheet
import com.flint.presentation.collectioncreate.component.CollectionCreateFilmDeleteModal
import com.flint.presentation.collectioncreate.component.CollectionCreateFilmItemList
import com.flint.presentation.collectioncreate.component.CollectionCreateThumbnail
import com.flint.presentation.collectioncreate.model.CollectionFilmUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CollectionCreateRoute(
    paddingValues: PaddingValues,
    navigateToAddFilm: () -> Unit,
) {
    val filmList =
        remember {
            CollectionFilmUiModel.dummyFilmList.toMutableStateList()
        }

    CollectionCreateScreen(
        thumbnailImageUrl = "",
        filmList = filmList.toImmutableList(),
        onRemoveFilm = { filmList.remove(it) },
        onBackClick = {},
        onGalleryClick = {},
        onCoverDeleteClick = {},
    )
}

@Composable
fun CollectionCreateScreen(
    thumbnailImageUrl: String,
    filmList: ImmutableList<CollectionFilmUiModel>,
    onRemoveFilm: (CollectionFilmUiModel) -> Unit,
    onBackClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onCoverDeleteClick: () -> Unit,
) {
    var titleText by remember { mutableStateOf("") }
    var contentText by remember { mutableStateOf("") }
    var isSheetVisible by remember { mutableStateOf(false) }
    var isModalVisible by remember { mutableStateOf(false) }
    var selectedFilm by remember { mutableStateOf<CollectionFilmUiModel?>(null) }
    var isPublic by remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = FlintTheme.colors.background),
    ) {
        FlintBackTopAppbar(onClick = onBackClick)

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            // 썸네일
            item {
                CollectionCreateThumbnail(
                    imageUrl = thumbnailImageUrl,
                    onClick = { isSheetVisible = true },
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
                        value = titleText,
                        placeholder = "컬렉션의 제목을 입력해주세요.",
                        onValueChanged = { titleText = it },
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
                        value = contentText,
                        placeholder = "컬렉션의 소개를 작성해주세요.",
                        onValueChanged = { contentText = it },
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
                            state =
                                if (isPublic ==
                                    true
                                ) {
                                    FlintButtonState.ColorOutline
                                } else if (isPublic ==
                                    false
                                ) {
                                    FlintButtonState.Disable
                                } else {
                                    FlintButtonState.Outline
                                },
                            onClick = { isPublic = true },
                            modifier = Modifier.weight(1f),
                        )

                        Spacer(Modifier.width(8.dp))

                        FlintIconButton(
                            text = "비공개",
                            iconRes = R.drawable.ic_lock,
                            state =
                                if (isPublic ==
                                    false
                                ) {
                                    FlintButtonState.ColorOutline
                                } else if (isPublic ==
                                    true
                                ) {
                                    FlintButtonState.Disable
                                } else {
                                    FlintButtonState.Outline
                                },
                            onClick = { isPublic = false },
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
                            text = "${filmList.size}/10",
                            color = FlintTheme.colors.white,
                            style = FlintTheme.typography.body2R14,
                        )
                    }
                }
            }

            // 작품 리스트
            items(
                items = filmList,
                key = { it.filmId },
            ) { film ->
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    CollectionCreateFilmItemList(
                        onCancelClick = {
                            selectedFilm = film
                            isModalVisible = true
                        },
                        imageUrl = film.imageUrl,
                        title = film.title,
                        director = film.director,
                        createdYear = film.createdYear,
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
                        onClick = {},
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
            state = FlintButtonState.Disable,
            onClick = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }

    if (isSheetVisible) {
        CollectionAddFilmBottomSheet(
            onGalleryClick = onGalleryClick,
            onCoverDeleteClick = onCoverDeleteClick,
            onDismiss = { isSheetVisible = false },
        )
    }

    if (isModalVisible) {
        CollectionCreateFilmDeleteModal(
            onConfirm = {
                selectedFilm?.let { onRemoveFilm(it) }
                selectedFilm = null
                isModalVisible = false
            },
            onDismiss = {
                selectedFilm = null
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
            thumbnailImageUrl = "",
            filmList = CollectionFilmUiModel.dummyFilmList,
            onRemoveFilm = {},
            onBackClick = {},
            onGalleryClick = {},
            onCoverDeleteClick = {},
        )
    }
}
