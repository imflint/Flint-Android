package com.flint.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.textfield.FlintSearchTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.content.BookmarkedContentItemModel
import com.flint.domain.model.content.BookmarkedContentListModel
import com.flint.domain.type.OttType
import com.flint.presentation.profile.component.CollectionCreateContentBookmark
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun SavedContentRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
) {
    // TODO: ViewModel 연결 후 실제 데이터로 교체
    val contents = remember { SavedContentPreviewData.FakeList }

    SavedContentScreen(
        contents = contents,
        navigateUp = navigateUp,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun SavedContentScreen(
    contents: ImmutableList<BookmarkedContentItemModel>,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchKeyword by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = FlintTheme.colors.background),
    ) {
        FlintBackTopAppbar(
            onClick = navigateUp,
            title = "저장 작품",
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlintSearchTextField(
            value = searchKeyword,
            onValueChanged = { searchKeyword = it },
            placeholder = "작품을 검색해보세요",
            modifier = Modifier.padding(horizontal = 16.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                },
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "총 ${contents.size}개",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.body1M16,
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(
                items = contents,
                key = { it.id },
            ) { content ->
                CollectionCreateContentBookmark(
                    onBookmarkClick = {},
                    onMoreClick = {},
                    isBookmarked = true,
                    bookmarkCount = 123,
                    imageUrl = content.imageUrl,
                    title = content.title,
                    director = "감독이름",
                    createdYear = content.year,
                    ottList = content.getOttSimpleList,
                )
            }
        }
    }
}

private object SavedContentPreviewData {
    val FakeList: ImmutableList<BookmarkedContentItemModel> = persistentListOf(
        BookmarkedContentItemModel(
            id = "0",
            title = "은하수를 여행하는 히치하이커를 위한 안내서",
            year = 2005,
            imageUrl = "",
            getOttSimpleList = listOf(
                OttType.Netflix,
                OttType.Disney,
                OttType.Tving,
            ),
        ),
        BookmarkedContentItemModel(
            id = "1",
            title = "해리포터와 불의잔",
            year = 2005,
            imageUrl = "",
            getOttSimpleList = listOf(
                OttType.Netflix,
                OttType.CoupangPlay
            ),
        ),
        BookmarkedContentItemModel(
            id = "2",
            title = "해리포터와 불의잔",
            year = 2005,
            imageUrl = "",
            getOttSimpleList = listOf(OttType.Netflix),
        ),
        BookmarkedContentItemModel(
            id = "3",
            title = "해리포터와 불의잔",
            year = 2005,
            imageUrl = "",
            getOttSimpleList = listOf(OttType.Netflix),
        ),
        BookmarkedContentItemModel(
            id = "4",
            title = "해리포터와 불의잔",
            year = 2005,
            imageUrl = "",
            getOttSimpleList = listOf(OttType.Netflix),
        ),
        BookmarkedContentItemModel(
            id = "5",
            title = "해리포터와 불의잔",
            year = 2005,
            imageUrl = "",
            getOttSimpleList = listOf(OttType.Netflix),
        ),
        BookmarkedContentItemModel(
            id = "6",
            title = "해리포터와 불의잔",
            year = 2005,
            imageUrl = "",
            getOttSimpleList = listOf(OttType.Netflix),
        ),
        BookmarkedContentItemModel(
            id = "7",
            title = "해리포터와 불의잔",
            year = 2005,
            imageUrl = "",
            getOttSimpleList = listOf(OttType.Netflix),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun SavedContentScreenPreview() {
    FlintTheme {
        SavedContentScreen(
            contents = SavedContentPreviewData.FakeList,
            navigateUp = {},
        )
    }
}
