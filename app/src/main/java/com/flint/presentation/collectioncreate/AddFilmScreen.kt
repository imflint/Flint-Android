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
import com.flint.core.designsystem.component.textfield.FlintSearchTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.collectioncreate.component.CollectionCreateFilmSelect

@Composable
fun AddFilmRoute(
    paddingValues: PaddingValues,
    navigateToCollectionCreate: () -> Unit,
) {
}

data class CollectionFilmUiModel(
    val filmId: Long,
    val imageUrl: String,
    val title: String,
    val director: String,
    val createdYear: String,
)

@Composable
fun AddFilmScreen(
    onBackClick: () -> Unit,
) {
    var selectedFilmIds by remember { mutableStateOf(setOf<Long>()) }
    var searchText by remember { mutableStateOf("") }

    val filmList = remember {
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
            CollectionFilmUiModel(
                filmId = 3L,
                imageUrl = "https://buly.kr/DEaVFRZ",
                title = "라라랜드",
                director = "데이미언 셔젤",
                createdYear = "2016",
            ),
        )
    }

    Column(
        modifier =Modifier
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

//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),

        FlintSearchTextField(
            placeholder = "추천하고 싶은 작품을 검색해보세요",
            value = searchText,
            onValueChanged = { searchText = it },
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn (
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // 작품 리스트
            items(
                items = filmList,
                key = { it.filmId },
            ) { film ->
                val isSelected = selectedFilmIds.contains(film.filmId)

                CollectionCreateFilmSelect(
                    onCheckClick = {
                        selectedFilmIds =
                            if (isSelected) {
                                selectedFilmIds - film.filmId
                            } else {
                                selectedFilmIds + film.filmId
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
fun AddFilmScreenPreview() {
    FlintTheme {
        AddFilmScreen(
            onBackClick = {}
        )
    }
}