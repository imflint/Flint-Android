package com.flint.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import com.flint.core.designsystem.theme.Colors
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
import com.flint.presentation.profile.uistate.ProfileSectionData
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
                    val activity = context.findActivity() as? MainActivity
                    if (activity != null) {
                        activity.restartApplication()
                    } else {
                        //TODO: Fallback: 앱 재시작이 불가능할 경우, 다른 처리 로직을 여기에 작성
                    }
                }
            }
        }
    }

    ProfileScreen(
        modifier = Modifier.padding(paddingValues),
        uiState = uiState,
        onBackClick = navigateUp,
        onCollectionItemClick = navigateToCollectionDetail,
        onContentItemClick = { contentId ->
            viewModel.getOttListPerContent(contentId)
        },
        onCreatedCollectionMoreClick = {
            navigateToCollectionList(
                CollectionListRouteType.CREATED,
                uiState.userId
            )
        },
        onSavedCollectionMoreClick = {
            navigateToCollectionList(
                CollectionListRouteType.SAVED,
                uiState.userId
            )
        },
        onEasterEggWithdraw = viewModel::easterEggWithdraw,
    )

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
    onContentItemClick: (contentId: String) -> Unit = {},
    onContentMoreClick: () -> Unit = {},
    onCreatedCollectionMoreClick: () -> Unit,
    onSavedCollectionMoreClick: () -> Unit,
    onEasterEggWithdraw: () -> Unit = {},
) {
    val userName = uiState.profile.nickname
    var topHeightPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val topHeightDp = with(density) { topHeightPx.toDp() }

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
                Box(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        topHeightPx = coordinates.size.height
                    }
                ) {
                    with(uiState.profile) {
                        ProfileTopSection(
                            userName = nickname,
                            profileUrl = profileImageUrl.orEmpty(),
                            isFliner = isFliner,
                            onEasterEggWithdraw = {
                                if (uiState.userId == null) {
                                    onEasterEggWithdraw()
                                }
                            },
                        )
                    }
                }
            }

            when (val sectionData = uiState.sectionData) {
                is UiState.Loading -> {
                    item {
                        FlintLoadingIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = topHeightDp / 2)
                        )
                    }
                }

                is UiState.Success -> {
                    item {
                        Spacer(Modifier.height(20.dp))

                        ProfileKeywordSection(
                            nickname = uiState.profile.nickname,
                            keywordList = sectionData.data.keywords,
                            onRefreshClick = onRefreshClick,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    item {
                        if (sectionData.data.savedCollections.collections.isNotEmpty()) {
                            Spacer(Modifier.height(48.dp))

                            CollectionSection(
                                title = "저장한 컬렉션",
                                description = "${userName}님이 저장한 컬렉션이에요",
                                onItemClick = onCollectionItemClick,
                                isAllVisible = true,
                                onAllClick = onSavedCollectionMoreClick,
                                collectionListModel = sectionData.data.savedCollections,
                            )
                        }
                    }

                    item {
                        Spacer(Modifier.height(48.dp))

                        SavedContentsSection(
                            title = "저장한 작품",
                            description = "${userName}님이 저장한 작품이에요",
                            contentModelList = sectionData.data.savedContents,
                            onItemClick = onContentItemClick,
                            isAllVisible = false,
                            onAllClick = {},
                        )
                    }
                }

                else -> {}
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
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onCollectionItemClick = {},
            onCreatedCollectionMoreClick = {},
            onSavedCollectionMoreClick = {}
        )
    }
}

private class ProfileUiStatePreviewParameterProvider : PreviewParameterProvider<ProfileUiState> {
    override val values: Sequence<ProfileUiState> = sequenceOf(
        // 로딩 상태
        ProfileUiState(
            userId = null,
            profile = UserProfileResponseModel(
                id = "",
                nickname = "닉네임",
                profileImageUrl = "",
                isFliner = false,
            ),
            sectionData = UiState.Loading
        ),
        // 내 프로필
        ProfileUiState(
            userId = null,
            profile = UserProfileResponseModel(
                id = "",
                nickname = "닉네임",
                profileImageUrl = "",
                isFliner = false,
            ),
            sectionData = UiState.Success(
                ProfileSectionData(
                    keywords = KeywordListModel.FakeList1,
                    createCollections = CollectionListModel.FakeList,
                    savedCollections = CollectionListModel.FakeList,
                    savedContents = BookmarkedContentListModel.FakeList,
                )
            ),
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
            sectionData = UiState.Success(
                ProfileSectionData(
                    keywords = KeywordListModel.FakeList3,
                    createCollections = CollectionListModel(),
                    savedCollections = CollectionListModel(),
                    savedContents = BookmarkedContentListModel.FakeList,
                )
            ),
        ),
    )
}
