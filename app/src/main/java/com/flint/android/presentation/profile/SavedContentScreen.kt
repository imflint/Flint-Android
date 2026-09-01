package com.flint.android.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.android.R
import com.flint.android.core.analytics.FlintEvent
import com.flint.android.core.analytics.LocalAnalyticsTracker
import com.flint.android.core.analytics.onItemImpression
import com.flint.android.core.common.util.UiState
import com.flint.android.core.designsystem.component.bottomsheet.OttListBottomSheet
import com.flint.android.core.designsystem.component.indicator.FlintLoadingIndicator
import com.flint.android.core.designsystem.component.modal.OneButtonModal
import com.flint.android.core.designsystem.component.toast.ShowToast
import com.flint.android.core.designsystem.component.textfield.FlintSearchTextField
import com.flint.android.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.android.core.designsystem.component.view.FlintSearchEmptyView
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.model.content.BookmarkedContentItemModel
import com.flint.android.domain.model.content.BookmarkedContentListModel
import com.flint.android.domain.model.ott.OttListModel
import com.flint.android.domain.model.ott.OttModel
import com.flint.android.domain.type.OttType
import com.flint.android.presentation.profile.component.CollectionCreateContentBookmark
import com.flint.android.presentation.profile.uistate.SavedContentUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun SavedContentRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: SavedContentViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showSaveToast by remember { mutableStateOf(false) }
    var showCancelToast by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is SavedContentSideEffect.ToggleBookmarkSuccess -> {
                    if (effect.isBookmarked) {
                        showSaveToast = true
                        showCancelToast = false
                    } else {
                        showCancelToast = true
                        showSaveToast = false
                    }
                }
            }
        }
    }

    SavedContentScreen(
        uiState = uiState,
        navigateUp = navigateUp,
        onSearchKeywordChanged = viewModel::updateSearchKeyword,
        onClearSearch = viewModel::clearSearchKeyword,
        onBookmarkClick = viewModel::toggleBookmark,
        onDismissRestrictionModal = viewModel::dismissBookmarkRestrictionModal,
        modifier = Modifier.padding(paddingValues),
    )

    if (showSaveToast) {
        ShowToast(
            text = "작품을 저장했어요",
            imageVector = null,
            paddingValues = paddingValues,
            yOffset = 12.dp,
            hide = { showSaveToast = false },
        )
    }

    if (showCancelToast) {
        ShowToast(
            text = "작품 저장이 취소되었어요",
            imageVector = null,
            paddingValues = paddingValues,
            yOffset = 12.dp,
            hide = { showCancelToast = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedContentScreen(
    uiState: SavedContentUiState,
    navigateUp: () -> Unit,
    onSearchKeywordChanged: (String) -> Unit,
    onClearSearch: () -> Unit,
    onBookmarkClick: (contentId: String) -> Unit,
    onDismissRestrictionModal: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showOttBottomSheet by remember { mutableStateOf(false) }
    var selectedOttList by remember { mutableStateOf<List<OttType>>(emptyList()) }

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
            value = uiState.searchKeyword,
            onValueChanged = onSearchKeywordChanged,
            placeholder = "작품을 검색해보세요",
            modifier = Modifier.padding(horizontal = 16.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                },
            ),
            onClearAction = onClearSearch,
        )

        // "총 n개"는 검색 결과가 있을 때만 노출
        if (uiState.contents is UiState.Success && uiState.filteredContents.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "총 ${uiState.filteredContents.size}개",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = FlintTheme.colors.gray100,
                style = FlintTheme.typography.body2R14,
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        when (uiState.contents) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    FlintLoadingIndicator()
                }
            }
            is UiState.Success -> {
                if (uiState.filteredContents.isEmpty()) {
                    // 검색 결과가 없을 때
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        FlintSearchEmptyView(
                            title = "작품을 찾을 수 없어요",
                        )
                    }
                } else {
                    SavedContentList(
                        contents = uiState.filteredContents,
                        onBookmarkClick = onBookmarkClick,
                        onMoreClick = { ottList ->
                            selectedOttList = ottList
                            showOttBottomSheet = true
                        },
                    )
                }
            }
            is UiState.Empty,
            is UiState.Failure -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    FlintSearchEmptyView(
                        title = "작품을 찾을 수 없어요",
                    )
                }
            }
        }
    }

    if (showOttBottomSheet && selectedOttList.isNotEmpty()) {
        OttListBottomSheet(
            ottList = OttListModel(otts = selectedOttList.map { OttModel(name = it.name) }),
            onDismiss = { showOttBottomSheet = false },
        )
    }

    // 저장 취소 제한 안내 모달 (저장 작품이 5개일 때 북마크 토글 시 노출)
    if (uiState.showBookmarkRestrictionModal) {
        OneButtonModal(
            title = "작품 저장을 취소할 수 없어요",
            message = "취향 키워드 분석을 위해\n최소 ${SavedContentUiState.MIN_REQUIRED_COUNT}개의 작품을 저장해주세요",
            buttonText = "확인",
            onConfirm = onDismissRestrictionModal,
            onDismiss = onDismissRestrictionModal,
            icon = R.drawable.ic_gradient_bookmark,
        )
    }
}

@Composable
private fun SavedContentList(
    contents: ImmutableList<BookmarkedContentItemModel>,
    onBookmarkClick: (contentId: String) -> Unit,
    onMoreClick: (ottList: List<OttType>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val analyticsTracker = LocalAnalyticsTracker.current

    // 같은 저장함 방문 안에서는 작품당 한 번만 보낸다.
    // 화면을 벗어나면 함께 사라지므로 다시 들어오면 새 방문으로 집계된다.
    val trackedContentIds = rememberSaveable { arrayListOf<String>() }
    if (contents.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            FlintSearchEmptyView(
                title = "작품을 찾을 수 없어요",
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            items = contents,
            key = { it.id },
        ) { content ->
            CollectionCreateContentBookmark(
                modifier = Modifier
                    .animateItem()
                    .onItemImpression(
                        key = content.id,
                        alreadyTracked = { id -> id in trackedContentIds },
                        onImpression = { id ->
                            trackedContentIds.add(id as String)
                            analyticsTracker.track(FlintEvent.ViewSavedContent(id))
                        },
                    ),
                onBookmarkClick = { onBookmarkClick(content.id) },
                onMoreClick = { onMoreClick(content.getOttSimpleList) },
                isBookmarked = content.isBookmarked,
                bookmarkCount = content.bookmarkCount,
                imageUrl = content.imageUrl,
                title = content.title,
                director = "감독이름",
                createdYear = content.year,
                ottList = content.getOttSimpleList,
            )
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
            bookmarkCount = 42,
            isBookmarked = true,
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
            bookmarkCount = 18,
            isBookmarked = true,
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
            bookmarkCount = 7,
            isBookmarked = false,
            getOttSimpleList = listOf(OttType.Netflix),
        ),
        BookmarkedContentItemModel(
            id = "3",
            title = "해리포터와 불의잔",
            year = 2005,
            imageUrl = "",
            bookmarkCount = 29,
            isBookmarked = true,
            getOttSimpleList = listOf(OttType.Netflix),
        ),
        BookmarkedContentItemModel(
            id = "4",
            title = "해리포터와 불의잔",
            year = 2005,
            imageUrl = "",
            bookmarkCount = 13,
            isBookmarked = false,
            getOttSimpleList = listOf(OttType.Netflix),
        ),
        BookmarkedContentItemModel(
            id = "5",
            title = "해리포터와 불의잔",
            year = 2005,
            imageUrl = "",
            bookmarkCount = 51,
            isBookmarked = true,
            getOttSimpleList = listOf(OttType.Netflix),
        ),
        BookmarkedContentItemModel(
            id = "6",
            title = "해리포터와 불의잔",
            year = 2005,
            imageUrl = "",
            bookmarkCount = 4,
            isBookmarked = false,
            getOttSimpleList = listOf(OttType.Netflix),
        ),
        BookmarkedContentItemModel(
            id = "7",
            title = "해리포터와 불의잔",
            year = 2005,
            imageUrl = "",
            bookmarkCount = 36,
            isBookmarked = true,
            getOttSimpleList = listOf(OttType.Netflix),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun SavedContentScreenPreview() {
    FlintTheme {
        SavedContentScreen(
            uiState = SavedContentUiState(
                contents = UiState.Success(
                    BookmarkedContentListModel(
                        totalCount = SavedContentPreviewData.FakeList.size,
                        contents = SavedContentPreviewData.FakeList,
                    ),
                ),
            ),
            navigateUp = {},
            onSearchKeywordChanged = {},
            onClearSearch = {},
            onBookmarkClick = {},
            onDismissRestrictionModal = {},
        )
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun SavedContentScreenEmptyPreview() {
    FlintTheme {
        SavedContentScreen(
            uiState = SavedContentUiState(contents = UiState.Empty),
            navigateUp = {},
            onSearchKeywordChanged = {},
            onClearSearch = {},
            onBookmarkClick = {},
            onDismissRestrictionModal = {},
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun SavedContentScreenLoadingPreview() {
    FlintTheme {
        SavedContentScreen(
            uiState = SavedContentUiState(contents = UiState.Loading),
            navigateUp = {},
            onSearchKeywordChanged = {},
            onClearSearch = {},
            onBookmarkClick = {},
            onDismissRestrictionModal = {},
        )
    }
}

@Preview(showBackground = true, name = "Restriction Modal")
@Composable
private fun SavedContentScreenRestrictionModalPreview() {
    FlintTheme {
        SavedContentScreen(
            uiState = SavedContentUiState(
                contents = UiState.Success(
                    BookmarkedContentListModel(
                        totalCount = SavedContentPreviewData.FakeList.size,
                        contents = SavedContentPreviewData.FakeList,
                    ),
                ),
                showBookmarkRestrictionModal = true,
            ),
            navigateUp = {},
            onSearchKeywordChanged = {},
            onClearSearch = {},
            onBookmarkClick = {},
            onDismissRestrictionModal = {},
        )
    }
}
