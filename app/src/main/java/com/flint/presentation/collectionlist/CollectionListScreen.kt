package com.flint.presentation.collectionlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.flint.core.designsystem.component.textfield.FlintSearchTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.CollectionDetailModel
import com.flint.presentation.collectionlist.component.CollectionFileItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CollectionListRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToCollectionDetail: () -> Unit,
) {
    CollectionListScreen(
        onBackClick = navigateUp,
        title = "전체 컬렉션",
        modifier = Modifier.padding(paddingValues),
        collections = persistentListOf(), // TODO: 변경 필요
    )
}

@Composable
private fun CollectionListScreen(
    onBackClick: () -> Unit,
    title: String,
    collections: ImmutableList<CollectionDetailModel>,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(FlintTheme.colors.background),
    ) {
        FlintBackTopAppbar(
            onClick = onBackClick,
            title = title,
            backgroundColor = FlintTheme.colors.background,
        )

        Spacer(Modifier.height(12.dp))

        FlintSearchTextField(
            placeholder = "컬렉션을 검색해보세요",
            value = text,
            onValueChanged = { text = it },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "총 ${collections.size}개",
            color = FlintTheme.colors.gray100,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier =
                Modifier
                    .padding(start = 20.dp)
                    .fillMaxSize(),
        ) {
            items(
                items = collections,
                key = { it.collectionId },
            ) { collection ->
                CollectionFileItem(
                    collection = collection,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CollectionListScreenPreview() {
    FlintTheme {
        CollectionListScreen(
            onBackClick = {},
            title = "전체 컬렉션",
            collections =
                List(6) { index ->
                    CollectionDetailModel.Fake.copy(
                        collectionId = index.toString(),
                    )
                }.toImmutableList(),
        )
    }
}
