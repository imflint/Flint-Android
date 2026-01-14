package com.flint.presentation.collectioncreate

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.flint.core.designsystem.component.button.FlintModalButton
import com.flint.core.designsystem.component.textfield.FlintLongTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.collectioncreate.component.CollectionAddFilmBottomSheet
import com.flint.presentation.collectioncreate.component.CollectionCreateThumbnail

//@Composable
//fun CollectionCreateRoute(
//    paddingValues: PaddingValues,
//    navigateToAddFilm: () -> Unit,
//) {
//    CollectionCreateScreen()
//}

@Composable
fun CollectionCreateScreen(
    thumbnailImageUrl: String,
    onThumbnailClick: () -> Unit,
    onBackClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onCoverDeleteClick: () -> Unit,
) {
    var titleText by remember { mutableStateOf("") }
    var contentText by remember { mutableStateOf("") }

    var isSheetVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = FlintTheme.colors.background)
    ) {
        //TopAppBar
        FlintBackTopAppbar(onClick = onBackClick)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            //썸네일
            CollectionCreateThumbnail(
                imageUrl = thumbnailImageUrl,
                onClick = { isSheetVisible = true },
            )

            Spacer(Modifier.height(36.dp))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                //컬렉션 제목 필드란
                Column {
                    Text(
                        text = "컬렉션 제목",
                        color = FlintTheme.colors.white,
                        style = FlintTheme.typography.head3M18
                    )

                    Spacer(Modifier.height(16.dp))

                    FlintLongTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = titleText,
                        placeholder = "컬렉션의 제목을 입력해주세요",
                        onValueChanged = { titleText = it },
                        maxLength = 20,
                        height = 40.dp
                    )
                }

                //컬렉션 소개 필드란
                Column {
                    Text(
                        text = "컬렉션 소개 (선택)",
                        color = FlintTheme.colors.white,
                        style = FlintTheme.typography.head3M18
                    )

                    Spacer(Modifier.height(16.dp))

                    FlintLongTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = contentText,
                        placeholder = "컬렉션의 주제를 작성해주세요",
                        onValueChanged = { contentText = it },
                        maxLength = 200,
                        height = 104.dp
                    )
                }

                //컬렉션 공개 여부
                Column {
                    Text(
                        text = "컬렉션 공개 여부",
                        color = FlintTheme.colors.white,
                        style = FlintTheme.typography.head3M18
                    )

                    Spacer(Modifier.height(16.dp))

                    Row {
                        FlintIconButton(
                            text = "공개",
                            iconRes = R.drawable.ic_share,
                            state = FlintButtonState.Outline,
                            onClick = {},
                            modifier = Modifier.weight(1f),
                        )

                        Spacer(Modifier.width(8.dp))

                        FlintIconButton(
                            text = "비공개",
                            iconRes = R.drawable.ic_lock,
                            state = FlintButtonState.Outline,
                            onClick = {},
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                //컬렉션 작품 추가
                Column {
                    Text(
                        text = "작품 추가",
                        color = FlintTheme.colors.white,
                        style = FlintTheme.typography.head3M18
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "최대 10개까지 추가할 수 있어요",
                            color = FlintTheme.colors.gray200,
                            style = FlintTheme.typography.body2R14
                        )
                        Text(
                            text = "0/10",
                            color = FlintTheme.colors.gray200,
                            style = FlintTheme.typography.body2R14
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                }

                FlintLargeButton(
                    text = "완료",
                    state = FlintButtonState.Disable,
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    if (isSheetVisible) {
        CollectionAddFilmBottomSheet(
            onGalleryClick = onGalleryClick,
            onCoverDeleteClick = onCoverDeleteClick,
            onDismiss = { isSheetVisible = false }
        )
    }
}

@Preview
@Composable
fun CollectionCreateScreenPreview() {
    FlintTheme {
        CollectionCreateScreen(
            thumbnailImageUrl = "",
            onThumbnailClick = {},
            onBackClick = {},
            onGalleryClick = {},
            onCoverDeleteClick = {}
        )
    }
}