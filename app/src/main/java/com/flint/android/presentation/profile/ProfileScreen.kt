package com.flint.android.presentation.profile

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.android.core.analytics.CollectionSource
import com.flint.android.core.common.extension.noRippleClickable
import com.flint.android.core.designsystem.interaction.flintIconClickable
import com.flint.android.R
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.flint.android.core.common.util.UiState
import com.flint.android.core.designsystem.component.bottomsheet.OttListBottomSheet
import com.flint.android.core.designsystem.component.indicator.FlintLoadingIndicator
import com.flint.android.core.designsystem.component.listView.CollectionSection
import com.flint.android.core.designsystem.component.listView.SavedContentsSection
import com.flint.android.core.designsystem.component.topappbar.FlintBasicTopAppbar
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.core.designsystem.theme.FlintTheme.colors
import com.flint.android.core.navigation.model.CollectionListRouteType
import com.flint.android.domain.model.collection.CollectionListModel
import com.flint.android.domain.model.content.BookmarkedContentListModel
import com.flint.android.domain.model.ott.OttListModel
import com.flint.android.domain.model.ott.OttModel
import com.flint.android.domain.model.user.KeywordListModel
import com.flint.android.domain.model.user.UserProfileResponseModel
import com.flint.android.presentation.profile.component.ProfileKeywordSection
import com.flint.android.presentation.profile.component.ProfileTopSection
import com.flint.android.presentation.profile.sideeffect.ProfileSideEffect
import com.flint.android.presentation.profile.uistate.ProfileSectionData
import com.flint.android.presentation.profile.uistate.ProfileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateToCollectionList: (routeType: CollectionListRouteType, userId: String?) -> Unit,
    navigateToSavedContentList: (userId: String?) -> Unit,
    navigateToCollectionDetail: (collectionId: String, source: CollectionSource) -> Unit,
    navigateToSetting: () -> Unit = {},
    shouldRefreshProfile: Boolean = false,
    onProfileRefreshed: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    var showOttListBottomSheet by remember { mutableStateOf(false) }
    var ottListModel by remember { mutableStateOf(OttListModel()) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(shouldRefreshProfile) {
        if (shouldRefreshProfile) {
            viewModel.reloadUserProfile()
            onProfileRefreshed()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is ProfileSideEffect.ShowOttListBottomSheet -> {
                    ottListModel = sideEffect.ottListModel
                    if (ottListModel.otts.isNotEmpty()) {
                        showOttListBottomSheet = true
                    }
                }
            }
        }
    }

    ProfileScreen(
        modifier = Modifier.padding(paddingValues),
        uiState = uiState,
        onBackClick = navigateUp,
        onCreatedCollectionItemClick = { collectionId ->
            navigateToCollectionDetail(collectionId, CollectionSource.MY_CREATED)
        },
        onSavedCollectionItemClick = { collectionId ->
            navigateToCollectionDetail(collectionId, CollectionSource.MY_SAVED)
        },
        onSettingsClick = navigateToSetting,
        onContentItemClick = { contentId ->
            val ottList = (uiState.sectionData as? UiState.Success)
                ?.data?.savedContents?.contents
                ?.find { it.id == contentId }?.getOttSimpleList ?: emptyList()
            ottListModel = OttListModel(otts = ottList.map { OttModel(name = it.name) })
            if (ottListModel.otts.isNotEmpty()) showOttListBottomSheet = true
        },
        onContentMoreClick = { navigateToSavedContentList(uiState.userId) },
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
        onRefreshClick = viewModel::recalculateKeywords,
    )

    if (showOttListBottomSheet) {
        OttListBottomSheet(
            ottList = ottListModel,
            onDismiss = { showOttListBottomSheet = false },
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
    onSettingsClick: () -> Unit = {},
    onCreatedCollectionItemClick: (collectionId: String) -> Unit,
    onSavedCollectionItemClick: (collectionId: String) -> Unit,
    onContentItemClick: (contentId: String) -> Unit = {},
    onContentMoreClick: () -> Unit = {},
    onCreatedCollectionMoreClick: () -> Unit,
    onSavedCollectionMoreClick: () -> Unit,
) {
    val userName = uiState.profile.nickname
    var topHeightPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val topHeightDp = with(density) { topHeightPx.toDp() }
    var showInfoModal by remember { mutableStateOf(false) }

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
                            isMyProfile = uiState.userId == null,
                            isRecalculatable = uiState.profile.keywordRecalculatable,
                            isRecalculating = uiState.isRecalculating,
                            showInfoModal = showInfoModal,
                            onInfoClick = { showInfoModal = !showInfoModal },
                            onRefreshClick = onRefreshClick,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    item {
                        if (sectionData.data.createCollections.collections.isNotEmpty()) {
                            Spacer(Modifier.height(48.dp))

                            CollectionSection(
                                title = "${userName}님의 컬렉션",
                                description = "${userName}님이 생성한 컬렉션이에요",
                                onItemClick = onCreatedCollectionItemClick,
                                isAllVisible = true,
                                onAllClick = onCreatedCollectionMoreClick,
                                collectionListModel = sectionData.data.createCollections,
                            )
                        }
                    }

                    item {
                        if (sectionData.data.savedCollections.collections.isNotEmpty()) {
                            Spacer(Modifier.height(48.dp))

                            CollectionSection(
                                title = "저장한 컬렉션",
                                description = "${userName}님이 저장한 컬렉션이에요",
                                onItemClick = onSavedCollectionItemClick,
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
                            isAllVisible = true,
                            onAllClick = onContentMoreClick,
                        )
                    }
                }

                else -> {}
            }
        }
        if (showInfoModal) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable { showInfoModal = false },
            )
        }
        FlintBasicTopAppbar(
            backgroundColor = Color.Transparent,
            navigationIcon = {
                if (uiState.userId != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                        contentDescription = null,
                        tint = FlintTheme.colors.white,
                        modifier = Modifier.flintIconClickable { onBackClick() },
                    )
                }
            },
            action = {
                if (uiState.userId == null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_setting),
                        contentDescription = "설정",
                        tint = FlintTheme.colors.white,
                        modifier = Modifier.flintIconClickable { onSettingsClick() },
                    )
                }
            },
        )
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
            onCreatedCollectionItemClick = {},
            onSavedCollectionItemClick = {},
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
