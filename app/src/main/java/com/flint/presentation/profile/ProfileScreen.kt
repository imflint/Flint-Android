package com.flint.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.listView.CollectionSection
import com.flint.core.designsystem.component.listView.SavedContentsSection
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.core.designsystem.theme.FlintTheme.colors
import com.flint.domain.model.CollectionModel
import com.flint.domain.model.ContentModel
import com.flint.domain.model.PreferenceKeywordModel
import com.flint.domain.type.UserRoleType
import com.flint.presentation.profile.component.ProfileKeywordSection
import com.flint.presentation.profile.component.ProfileTopSection
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ProfileRoute(
    paddingValues: PaddingValues,
    navigateToCollectionList: () -> Unit,
    navigateToSavedFilmList: () -> Unit,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
) {
    ProfileScreen(
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
private fun ProfileScreen(
    userName: String = "",
    userRole: UserRoleType = UserRoleType.FLING,
    keywordList: ImmutableList<PreferenceKeywordModel> = persistentListOf(),
    createCollectionModelList: ImmutableList<CollectionModel> = persistentListOf(),
    savedCollectionModelList: ImmutableList<CollectionModel> = persistentListOf(),
    savedContentModelList: ImmutableList<ContentModel> = persistentListOf(),
    onCollectionItemClick: (String) -> Unit = {},
    onFilmItemClick: (contentId: Long) -> Unit = {},
    onCollectionMoreClick: () -> Unit = {},
    onFilmMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier, // TODO: 위치 조정
) {
    LazyColumn(
        overscrollEffect = null,
        contentPadding = PaddingValues(bottom = 70.dp),
        modifier =
            modifier
                .background(colors.background)
                .fillMaxSize(),
    ) {
        item {
            ProfileTopSection(
                userName = userName,
                profileUrl = "",
                isFliner = (userRole == UserRoleType.FLINER),
            )
        }

        item {
            Spacer(Modifier.height(20.dp))

            ProfileKeywordSection(
                nickname = userName,
                keywordList = keywordList,
                onRefreshClick = {},
                modifier =
                    Modifier.fillMaxWidth(),
            )
        }

        item {
            Spacer(Modifier.height(48.dp))

            CollectionSection(
                title = "${userName}님의 컬렉션",
                description = "${userName}님이 생성한 컬렉션이에요",
                onItemClick = onCollectionItemClick,
                isAllVisible = true,
                onAllClick = onCollectionMoreClick,
                collectionModelList = createCollectionModelList,
            )
        }

        item {
            Spacer(Modifier.height(48.dp))

            CollectionSection(
                title = "저장한 컬렉션",
                description = "${userName}님이 저장한 컬렉션이에요",
                onItemClick = onCollectionItemClick,
                isAllVisible = true,
                onAllClick = onCollectionMoreClick,
                collectionModelList = savedCollectionModelList,
            )
        }

        item {
            Spacer(Modifier.height(48.dp))

            SavedContentsSection(
                title = "저장한 작품",
                description = "${userName}님이 저장한 작품이에요",
                isAllVisible = true,
                onAllClick = onFilmMoreClick,
                contentModelList = savedContentModelList,
                onItemClick = onFilmItemClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    FlintTheme {
        ProfileScreen(
            userName = "안두콩",
            keywordList = PreferenceKeywordModel.FakeList1,
            modifier = Modifier.fillMaxSize(),
            createCollectionModelList = CollectionModel.FakeList,
            savedCollectionModelList = CollectionModel.FakeList,
            savedContentModelList = ContentModel.FakeList,
        )
    }
}
