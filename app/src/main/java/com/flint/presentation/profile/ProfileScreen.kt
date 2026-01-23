package com.flint.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.core.common.extension.findActivity
import com.flint.core.common.util.UiState
import com.flint.core.designsystem.component.bottomsheet.OttListBottomSheet
import com.flint.core.designsystem.component.indicator.FlintLoadingIndicator
import com.flint.core.designsystem.component.listView.CollectionSection
import com.flint.core.designsystem.component.listView.SavedContentsSection
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.core.designsystem.theme.FlintTheme.colors
import com.flint.core.navigation.model.CollectionListRouteType
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.content.BookmarkedContentListModel
import com.flint.domain.model.ott.OttListModel
import com.flint.domain.model.user.KeywordListModel
import com.flint.domain.model.user.UserProfileResponseModel
import com.flint.presentation.MainActivity
import com.flint.presentation.profile.component.ProfileKeywordSection
import com.flint.presentation.profile.component.ProfileTopSection
import com.flint.presentation.profile.sideeffect.ProfileSideEffect
import com.flint.presentation.profile.uistate.ProfileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToCollectionList: (routeType: CollectionListRouteType, userId: String?) -> Unit,
    navigateToSavedContentList: () -> Unit, // TODO: 스프린트에서 구현
    navigateToCollectionDetail: (collectionId: String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    var showOttListBottomSheet by remember { mutableStateOf(false) }
    var ottListModel by remember { mutableStateOf(OttListModel()) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.getProfile()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is ProfileSideEffect.ShowOttListBottomSheet -> {
                    ottListModel = sideEffect.ottListModel
                    showOttListBottomSheet = true
                }
                is ProfileSideEffect.WithdrawSuccess -> {
                    (context.findActivity() as? MainActivity)?.restartApplication()
                }
            }
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            FlintLoadingIndicator()
        }

        is UiState.Success -> {
            ProfileScreen(
                modifier = Modifier.padding(paddingValues),
                uiState = state.data,
                onBackClick = navigateUp,
                onCollectionItemClick = navigateToCollectionDetail,
                onContentItemClick = { contentId ->
                    viewModel.getOttListPerContent(contentId)
                },
                onCreatedCollectionMoreClick =  {
                    navigateToCollectionList(
                        CollectionListRouteType.CREATED,
                        state.data.userId
                    )
                },
                onSavedCollectionMoreClick = {
                    navigateToCollectionList(
                        CollectionListRouteType.SAVED,
                        state.data.userId
                    )
                },
                onEasterEggWithdraw = viewModel::easterEggWithdraw,
            )
        }

        else -> {}
    }

    if (showOttListBottomSheet) {
        OttListBottomSheet(
            ottList = ottListModel,
            onDismiss = { showOttListBottomSheet = false },
            onMoveClick = { url ->
                uriHandler.openUri(url)
            },
            sheetState = sheetState,
        )
    }
}

@Composable
private fun ProfileScreen(
    uiState: ProfileUiState,
    modifier: Modifier = Modifier,
    onRefreshClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onCollectionItemClick: (collectionId: String) -> Unit,
    onContentItemClick: (contentId: String) -> Unit = {}, // TODO: 바텀시트 띄우기
    onContentMoreClick: () -> Unit = {},
    onCreatedCollectionMoreClick: () -> Unit,
    onSavedCollectionMoreClick: () -> Unit,
    onEasterEggWithdraw: () -> Unit = {},
) {
    val userName = uiState.profile.nickname

    Box(
        modifier = modifier
            .background(colors.background)
            .fillMaxSize(),
    ) {
        LazyColumn(
            overscrollEffect = null,
            contentPadding = PaddingValues(bottom = 70.dp),
        ) {
            item {
                with(uiState.profile) {
                    ProfileTopSection(
                        userName = nickname,
                        profileUrl = profileImageUrl.orEmpty(),
                        isFliner = isFliner,
                        onEasterEggWithdraw = onEasterEggWithdraw,
                    )
                }
            }

            item {
                Spacer(Modifier.height(20.dp))

                ProfileKeywordSection(
                    nickname = uiState.profile.nickname,
                    keywordList = uiState.keywords,
                    onRefreshClick = onRefreshClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                if (uiState.createCollections.collections.isNotEmpty()) {
                    Spacer(Modifier.height(48.dp))

                    CollectionSection(
                        title = "${userName}님의 컬렉션",
                        description = "${userName}님이 생성한 컬렉션이에요",
                        onItemClick = onCollectionItemClick,
                        isAllVisible = true,
                        onAllClick = onCreatedCollectionMoreClick,
                        collectionListModel = uiState.createCollections,
                    )
                }
            }

            item {
                if (uiState.savedCollections.collections.isNotEmpty()) {
                    Spacer(Modifier.height(48.dp))

                    CollectionSection(
                        title = "저장한 컬렉션",
                        description = "${userName}님이 저장한 컬렉션이에요",
                        onItemClick = onCollectionItemClick,
                        isAllVisible = true,
                        onAllClick = onSavedCollectionMoreClick,
                        collectionListModel = uiState.savedCollections,
                    )
                }
            }

            item {
                Spacer(Modifier.height(48.dp))

                SavedContentsSection(
                    title = "저장한 작품",
                    description = "${userName}님이 저장한 작품이에요",
                    contentModelList = uiState.savedContents,
                    onItemClick = onContentItemClick,
                    isAllVisible = false,
                    onAllClick = {},
                )
            }
        }
        if (uiState.userId != null) {
            FlintBackTopAppbar(
                onClick = onBackClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview(
    @PreviewParameter(ProfileUiStatePreviewParameterProvider::class) uiState: ProfileUiState,
) {
    FlintTheme {
        ProfileScreen(
            uiState = uiState,
            onCollectionItemClick = {},
            onCreatedCollectionMoreClick = {},
            onSavedCollectionMoreClick = {}
        )
    }
}

private class ProfileUiStatePreviewParameterProvider : PreviewParameterProvider<ProfileUiState> {
    override val values: Sequence<ProfileUiState> = sequenceOf(
        // 내 프로필
        ProfileUiState(
            userId = null,
            profile = UserProfileResponseModel(
                id = "",
                nickname = "닉네임",
                profileImageUrl = "",
                isFliner = false,
            ),
            keywords = KeywordListModel.FakeList1,
            createCollections = CollectionListModel.FakeList,
            savedCollections = CollectionListModel.FakeList,
            savedContents = BookmarkedContentListModel.FakeList,
        ),
        // 다른 사용자 프로필
        ProfileUiState(
            userId = "1",
            profile = UserProfileResponseModel(
                id = "",
                nickname = "닉네임",
                profileImageUrl = "",
                isFliner = true,
            ),
            keywords = KeywordListModel.FakeList3,
            createCollections = CollectionListModel(),
            savedCollections = CollectionListModel(),
            savedContents = BookmarkedContentListModel.FakeList,
        ),
    )
}
