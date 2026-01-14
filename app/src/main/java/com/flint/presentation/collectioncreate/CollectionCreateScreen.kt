package com.flint.presentation.collectioncreate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.flint.presentation.collectioncreate.component.CollectionCreateFilmItemList
import com.flint.presentation.collectioncreate.component.CollectionCreateThumbnail

/**
 * Provides the composable route that displays the collection creation screen.
 *
 * Renders CollectionCreateScreen with an empty thumbnail and no-op callbacks.
 *
 * @param paddingValues Window insets or parent padding to be applied to the route's content (currently unused).
 * @param navigateToAddFilm Callback intended to navigate to the "add film" flow (currently not invoked).
 */
@Composable
fun CollectionCreateRoute(
    paddingValues: PaddingValues,
    navigateToAddFilm: () -> Unit,
) {
    CollectionCreateScreen(
        thumbnailImageUrl = "",
        onBackClick = {},
        onGalleryClick = {},
        onCoverDeleteClick = {},
    )
}

data class CollectionFilmUiModel(
    val filmId: Long,
    val imageUrl: String,
    val title: String,
    val director: String,
    val createdYear: String,
)

/**
 * Displays the collection creation screen allowing the user to compose a new collection.
 *
 * The UI includes a thumbnail (tapping opens a bottom sheet), inputs for title and introduction,
 * a visibility toggle, a list of added films with a displayed count/10 and the ability to remove items,
 * an "add film" action, and a completion button. When the thumbnail is tapped a bottom sheet is shown
 * that can invoke gallery or cover-delete actions.
 *
 * @param thumbnailImageUrl URL of the current collection thumbnail to display.
 * @param onBackClick Called when the top app bar back action is triggered.
 * @param onGalleryClick Called when the user selects the gallery option from the bottom sheet.
 * @param onCoverDeleteClick Called when the user selects the delete-cover option from the bottom sheet.
 */
@Composable
fun CollectionCreateScreen(
    thumbnailImageUrl: String,
    onBackClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onCoverDeleteClick: () -> Unit,
) {
    var titleText by remember { mutableStateOf("") }
    var contentText by remember { mutableStateOf("") }
    var isSheetVisible by remember { mutableStateOf(false) }
    var isPublic by remember { mutableStateOf(true) }

    val filmList =
        remember {
            mutableStateListOf(
                CollectionFilmUiModel(
                    filmId = 1L,
                    imageUrl = "https://buly.kr/DEaVFRZ",
                    title = "해리포터 불의 잔",
                    director = "마이크 뉴웰",
                    createdYear = "2005",
                ),
                CollectionFilmUiModel(
                    filmId = 2L,
                    imageUrl = "https://buly.kr/DEaVFRZ",
                    title = "인터스텔라",
                    director = "크리스토퍼 놀란",
                    createdYear = "2014",
                ),
            )
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(color = FlintTheme.colors.background),
    ) {
        FlintBackTopAppbar(onClick = onBackClick)

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // 썸네일
            item {
                CollectionCreateThumbnail(
                    imageUrl = thumbnailImageUrl,
                    onClick = { isSheetVisible = true },
                )
            }

            // 컬렉션 제목
            item {
                Column {
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
                Column {
                    Text(
                        text = "컬렉션 소개 (선택)",
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
                Column {
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
                            state = FlintButtonState.Outline,
                            onClick = { isPublic = true },
                            modifier = Modifier.weight(1f),
                        )
                        Spacer(Modifier.width(8.dp))
                        FlintIconButton(
                            text = "비공개",
                            iconRes = R.drawable.ic_lock,
                            state = FlintButtonState.Outline,
                            onClick = { isPublic = false },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            // 작품 추가 헤더
            item {
                Column {
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
                            color = FlintTheme.colors.gray200,
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
                Column {
                    CollectionCreateFilmItemList(
                        onCancelClick = {
                            filmList.removeAll { it.filmId == film.filmId }
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
                Column {
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
}

/**
 * Preview of CollectionCreateScreen rendered inside the app theme.
 *
 * Renders the screen with an empty thumbnail and no-op callbacks so the layout
 * and visual appearance can be inspected in Android Studio's preview.
 */
@Preview
@Composable
fun CollectionCreateScreenPreview() {
    FlintTheme {
        CollectionCreateScreen(
            thumbnailImageUrl = "",
            onBackClick = {},
            onGalleryClick = {},
            onCoverDeleteClick = {},
        )
    }
}