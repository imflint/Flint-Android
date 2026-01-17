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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.image.SelectedFilmItem
import com.flint.core.designsystem.component.textfield.FlintSearchTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.collectioncreate.component.CollectionCreateFilmSelect

@Composable
fun AddFilmRoute(
    paddingValues: PaddingValues,
    navigateToCollectionCreate: () -> Unit,
) {
    AddFilmScreen(
        onBackClick = {},
    )
}

data class CollectionFilmUiModel(
    val filmId: Long,
    val imageUrl: String,
    val title: String,
    val director: String,
    val createdYear: String,
)

@Composable
fun AddFilmScreen(onBackClick: () -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var selectedFilms = remember { mutableStateListOf<CollectionFilmUiModel>() }

    val filmList =
        remember {
            mutableStateListOf(
                CollectionFilmUiModel(1L, "https://buly.kr/DEaVFRZ", "해리포터 불의 잔", "마이크 뉴웰", "2005"),
                CollectionFilmUiModel(2L, "https://buly.kr/2UkIDen", "인터스텔라", "크리스토퍼 놀란", "2014"),
                CollectionFilmUiModel(3L, "https://buly.kr/FAeqqRB", "라라랜드", "데이미언 셔젤", "2016"),
                CollectionFilmUiModel(4L, "https://buly.kr/DPVH2Ob", "라라랜드", "데이미언 셔젤", "2016"),
                CollectionFilmUiModel(5L, "https://buly.kr/DEaVFRZ", "라라랜드", "데이미언 셔젤", "2016"),
                CollectionFilmUiModel(6L, "https://buly.kr/DEaVFRZ", "라라랜드", "데이미언 셔젤", "2016"),
                CollectionFilmUiModel(7L, "https://buly.kr/DEaVFRZ", "라라랜드", "데이미언 셔젤", "2016"),
                CollectionFilmUiModel(8L, "https://buly.kr/DEaVFRZ", "라라랜드", "데이미언 셔젤", "2016"),
            )
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(color = FlintTheme.colors.background),
    ) {
        FlintBackTopAppbar(
            onClick = onBackClick,
            title = "작품 추가하기",
            actionText = "추가",
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

        if (selectedFilms.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    items = selectedFilms,
                    key = { it.filmId },
                ) { film: CollectionFilmUiModel ->
                    SelectedFilmItem(
                        imageUrl = film.imageUrl,
                        onRemoveClick = {
                            selectedFilms.remove(film)
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
                items = filmList,
                key = { it.filmId },
            ) { film ->
                val isSelected = selectedFilms.contains(film)

                CollectionCreateFilmSelect(
                    onCheckClick = {
                        if (isSelected) {
                            selectedFilms.remove(film)
                        } else {
                            selectedFilms.add(film)
                        }
                    },
                    isSelected = isSelected,
                    imageUrl = film.imageUrl,
                    title = film.title,
                    director = film.director,
                    createdYear = film.createdYear,
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddFilmScreenPreview() {
    FlintTheme {
        AddFilmScreen(
            onBackClick = {},
        )
    }
}
