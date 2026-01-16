package com.flint.presentation.collectionlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.AuthorModel
import com.flint.domain.model.CollectionDetailModel
import com.flint.presentation.collectionlist.component.CollectionFileItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CollectionListRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
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
            contentPadding = PaddingValues(10.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier =
                Modifier
                    .padding(horizontal = 10.dp),
        ) {
            items(
                items = collections,
                key = { it.collectionId },
            ) { collection ->
                Box(modifier = Modifier.fillMaxSize(1f).align(Alignment.CenterHorizontally)) {
                    CollectionFileItem(
                        collection = collection,
                        modifier =
                            Modifier
                                .align(Alignment.Center),
                    )
                }
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
                persistentListOf(
                    CollectionDetailModel.Fake.copy(
                        collectionId = "1",
                        collectionTitle = "2024 인생 영화 모음",
                        collectionContent = "올해 본 영화 중 최고만 모았습니다",
                        isBookmarked = true,
                        bookmarkCount = 128,
                        author = AuthorModel.Fake.copy(nickname = "닉네임은여덟글자"),
                    ),
                    CollectionDetailModel.Fake.copy(
                        collectionId = "2",
                        collectionTitle = "혼자 보기 아까운 숨은 명작",
                        collectionContent = "알려지지 않은 갓작들",
                        isBookmarked = false,
                        bookmarkCount = 56,
                        author = AuthorModel.Fake.copy(nickname = "시네필"),
                    ),
                    CollectionDetailModel.Fake.copy(
                        collectionId = "3",
                        collectionTitle = "비 오는 날 보기 좋은 영화",
                        collectionContent = "감성 충전용 영화 컬렉션입니다. 우울할 때 보면 좋아요.",
                        isBookmarked = true,
                        bookmarkCount = 234,
                        author = AuthorModel.Fake.copy(nickname = "무비러버"),
                    ),
                    CollectionDetailModel.Fake.copy(
                        collectionId = "4",
                        collectionTitle = "넷플릭스 정주행 리스트",
                        collectionContent = "주말에 몰아보기 좋은 시리즈",
                        isBookmarked = false,
                        bookmarkCount = 89,
                        author = AuthorModel.Fake,
                    ),
                    CollectionDetailModel.Fake.copy(
                        collectionId = "5",
                        collectionTitle = "SF 덕후 추천작",
                        collectionContent = "우주와 미래를 다룬 SF 명작 모음",
                        isBookmarked = true,
                        bookmarkCount = 412,
                        author = AuthorModel.Fake.copy(nickname = "SF매니아"),
                    ),
                    CollectionDetailModel.Fake.copy(
                        collectionId = "6",
                        collectionTitle = "첫 데이트용 무난한 영화",
                        collectionContent = "어색한 분위기를 풀어줄 영화들",
                        isBookmarked = false,
                        bookmarkCount = 67,
                        author = AuthorModel.Fake.copy(nickname = "연애고수"),
                    ),
                ),
        )
    }
}
