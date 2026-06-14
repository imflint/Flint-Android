package com.flint.presentation.collectiondetail

import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.core.common.util.UiState
import com.flint.core.designsystem.component.collection.PeopleBottomSheet
import com.flint.core.designsystem.component.indicator.FlintLoadingIndicator
import com.flint.core.designsystem.component.progressbar.UnderImageProgressBar
import com.flint.core.designsystem.component.toast.ShowSaveToast
import com.flint.core.designsystem.component.toast.ShowToast
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.core.navigation.model.CollectionListRouteType
import com.flint.domain.model.bookmark.CollectionBookmarkUsersModel
import com.flint.domain.model.collection.CollectionDetailModelNew
import com.flint.domain.model.content.ContentModelNew
import com.flint.domain.type.UserRoleType
import com.flint.presentation.collectiondetail.component.CollectionDetailDescription
import com.flint.presentation.collectiondetail.component.CollectionDetailThumbnail
import com.flint.presentation.collectiondetail.component.Content
import com.flint.presentation.collectiondetail.component.PeopleWhoSavedThisCollection
import com.flint.presentation.collectiondetail.sideeffect.CollectionDetailSideEffect
import com.flint.presentation.collectiondetail.uistate.CollectionDetailUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.format.DateTimeFormatter

private const val DATE_FORMAT_TO_SHOW = "yyyy. MM. dd."

@Composable
fun CollectionDetailRoute(
    paddingValues: PaddingValues,
    navigateToCollectionList: (CollectionListRouteType) -> Unit,
    navigateToProfile: (authorId: String) -> Unit,
    navigateUp: () -> Unit,
    targetImageUrl: String? = null,
    viewModel: CollectionDetailViewModel = hiltViewModel(),
) {
    val uiState: UiState<CollectionDetailUiState> by viewModel.uiState.collectAsStateWithLifecycle()
    var showCollectionCancelToast: Boolean by remember { mutableStateOf(false) }
    var showCollectionSaveToast: Boolean by remember { mutableStateOf(false) }
    var showContentSaveToast: Boolean by remember { mutableStateOf(false) }
    var showContentCancelToast: Boolean by remember { mutableStateOf(false) }

    when (val uiState = uiState) {
        UiState.Loading -> {
            FlintLoadingIndicator()
        }

        is UiState.Success<CollectionDetailUiState> -> {
            val collectionDetail: CollectionDetailModelNew = uiState.data.collectionDetail
            val collectionBookmarkUsers: ImmutableList<CollectionBookmarkUsersModel.User> =
                uiState.data.collectionBookmarkUsers.userList

            CollectionDetailScreen(
                paddingValues = paddingValues,
                targetImageUrl = targetImageUrl,
                thumbnailUrl = collectionDetail.thumbnailUrl,
                title = collectionDetail.title,
                isBookmarked = collectionDetail.isBookmarked,
                authorNickname = collectionDetail.author.nickname,
                authorUserRoleType = collectionDetail.author.userRole,
                createdAt = collectionDetail.createdAt.format(
                    DateTimeFormatter.ofPattern(DATE_FORMAT_TO_SHOW)
                ),
                description = collectionDetail.description,
                contents = collectionDetail.contents,
                people = collectionBookmarkUsers,
                onSaveDoneButtonClick = viewModel::toggleCollectionBookmark,
                onSaveNoneButtonClick = viewModel::toggleCollectionBookmark,
                navigateUp = navigateUp,
                onBookmarkIconClick = viewModel::toggleContentBookmark,
                onSpoilClick = viewModel::spoil,
                onAuthorNicknameClick = { navigateToProfile(collectionDetail.author.id) },
                onAuthorClick = navigateToProfile,
            )
        }

        else -> {}
    }

    if (showCollectionCancelToast) {
        ShowToast(
            text = "컬렉션 저장이 취소되었어요",
            imageVector = null,
            paddingValues = paddingValues,
            yOffset = 12.dp,
            hide = {
                showCollectionCancelToast = false
            }
        )
    }

    if (showCollectionSaveToast) {
        ShowSaveToast(
            navigateToSavedCollection = {
                navigateToCollectionList(CollectionListRouteType.SAVED)
            },
            paddingValues = paddingValues,
            yOffset = 12.dp,
            hide = {
                showCollectionSaveToast = false
            })
    }

    if (showContentSaveToast) {
        ShowToast(
            text = "작품을 저장했어요",
            imageVector = null,
            paddingValues = paddingValues,
            yOffset = 12.dp,
            hide = {
                showContentSaveToast = false
            }
        )
    }

    if (showContentCancelToast) {
        ShowToast(
            text = "작품 저장이 취소되었어요",
            imageVector = null,
            paddingValues = paddingValues,
            yOffset = 12.dp,
            hide = {
                showContentCancelToast = false
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { event: CollectionDetailSideEffect ->
            when (event) {
                CollectionDetailSideEffect.ToggleCollectionBookmarkFailure -> {
                    // TODO: 컬렉션 저장 실패 다이얼로그 띄우기
                }

                is CollectionDetailSideEffect.ToggleCollectionBookmarkSuccess -> {
                    if (event.isBookmarked) {
                        showCollectionSaveToast = true
                        showCollectionCancelToast = false
                    } else {
                        showCollectionCancelToast = true
                        showCollectionSaveToast = false
                    }
                }

                is CollectionDetailSideEffect.ToggleContentBookmarkSuccess -> {
                    if (event.isBookmarked) {
                        showContentSaveToast = true
                        showContentCancelToast = false
                    } else {
                        showContentCancelToast = true
                        showContentSaveToast = false
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionDetailScreen(
    paddingValues: PaddingValues,
    thumbnailUrl: String,
    title: String,
    isBookmarked: Boolean,
    authorNickname: String,
    authorUserRoleType: UserRoleType,
    createdAt: String,
    description: String,
    contents: ImmutableList<ContentModelNew>,
    people: ImmutableList<CollectionBookmarkUsersModel.User>,
    onSaveDoneButtonClick: () -> Unit,
    onSaveNoneButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    onBookmarkIconClick: (String) -> Unit,
    onSpoilClick: (String) -> Unit,
    onAuthorNicknameClick: () -> Unit,
    onAuthorClick: (authorId: String) -> Unit,
    targetImageUrl: String? = null,
) {
    CompositionLocalProvider(
        LocalOverscrollFactory provides null,
    ) {
        var showPeopleBottomSheet: Boolean by remember { mutableStateOf(false) }
        val scrollState: ScrollState = rememberScrollState()
        var thumbnailHeight: Int by remember { mutableIntStateOf(0) }
        val contentPositions: MutableMap<String, Int> = remember { mutableMapOf() }

        val scrollProgress: Float =
            if (scrollState.maxValue > 0) {
                scrollState.value.toFloat() / scrollState.maxValue
            } else {
                0f
            }

        val isProgressBarSticky: Boolean = scrollState.value >= thumbnailHeight

        LaunchedEffect(Unit) {
            if (targetImageUrl == null) return@LaunchedEffect
            val targetPosition: Int = contentPositions[targetImageUrl] ?: return@LaunchedEffect

            scrollState.animateScrollTo(targetPosition)
        }

        if (showPeopleBottomSheet) {
            PeopleBottomSheet(
                people = people,
                onAuthorClick = { userId: String ->
                    showPeopleBottomSheet = false
                    onAuthorClick(userId)
                },
                onDismiss = { showPeopleBottomSheet = false },
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = FlintTheme.colors.background),
        ) {
//            FlintBackTopAppbar(
//                onClick = navigateUp,
//                backgroundColor = Color.Transparent,
//            )

            Box {
                FlintBackTopAppbar(
                    onClick = navigateUp,
                    backgroundColor = Color.Transparent,
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(bottom = 24.dp),
                ) {
                    CollectionDetailThumbnail(
                        thumbnailImage = thumbnailUrl,
                        title = title,
                        isBookmarked = isBookmarked,
                        onSaveDoneButtonClick = onSaveDoneButtonClick,
                        onSaveNoneButtonClick = onSaveNoneButtonClick,
                        modifier = Modifier.onGloballyPositioned { coordinates: LayoutCoordinates ->
                            thumbnailHeight = coordinates.size.height
                        },
                    )

                    if (!isProgressBarSticky) {
                        UnderImageProgressBar(progress = scrollProgress)
                    } else {
                        // sticky 상태일 때 공간 유지
                        Spacer(Modifier.height(5.dp))
                    }

                    Spacer(Modifier.height(24.dp))

                    CollectionDetailDescription(
                        authorNickname = authorNickname,
                        authorUserRoleType = authorUserRoleType,
                        createdAt = createdAt,
                        collectionContent = description,
                        onAuthorNicknameClick = onAuthorNicknameClick,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                    )

                    Spacer(Modifier.height(48.dp))

                    contents.forEach { content: ContentModelNew ->
                        Content(
                            content = content,
                            onBookmarkIconClick = onBookmarkIconClick,
                            onSpoilClick = onSpoilClick,
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                contentPositions[content.imageUrl] =
                                    coordinates.positionInParent().y.toInt()
                            },
                        )
                    }

                    if (people.isNotEmpty()) {
                        PeopleWhoSavedThisCollection(
                            people = people,
                            onMoreClick = { showPeopleBottomSheet = true },
                        )
                    }
                }

                // Sticky ProgressBar
                if (isProgressBarSticky) {
                    UnderImageProgressBar(
                        progress = scrollProgress,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

private data class ScreenPreviewData(
    val thumbnailUrl: String,
    val title: String,
    val isBookmarked: Boolean,
    val authorNickname: String,
    val authorUserRoleType: UserRoleType,
    val contents: ImmutableList<ContentModelNew>,
    val people: ImmutableList<CollectionBookmarkUsersModel.User>,
)

private class ScreenPreviewProvider : PreviewParameterProvider<ScreenPreviewData> {
    private val sampleContent =
        ContentModelNew(
            id = "0",
            title = "드라마 제목",
            year = 2000,
            imageUrl = "",
            director = "감독 이름",
            reason = "달라진 온도\n-\n같은 구도에 채도를 달리해 변해버린 사랑을 시각적으로 담아낸 장면들",
            isBookmarked = true,
            isSpoiler = false,
            bookmarkCount = 9
        )

    private val samplePeople =
        persistentListOf(
            CollectionBookmarkUsersModel.User(
                userId = "1",
                nickName = "유저1",
                profileImageUrl = "",
                userRole = UserRoleType.FLING,
            ),
            CollectionBookmarkUsersModel.User(
                userId = "2",
                nickName = "유저2",
                profileImageUrl = "",
                userRole = UserRoleType.FLINER,
            ),
            CollectionBookmarkUsersModel.User(
                userId = "3",
                nickName = "유저3",
                profileImageUrl = "",
                userRole = UserRoleType.FLING,
            ),
        )

    override val values: Sequence<ScreenPreviewData> =
        sequenceOf(
            ScreenPreviewData(
                thumbnailUrl = "",
                title = "한번 보면 못 빠져나오는 여운남는 사랑이야기",
                isBookmarked = true,
                authorNickname = "키카",
                authorUserRoleType = UserRoleType.FLINER,
                contents = persistentListOf(sampleContent, sampleContent.copy(isSpoiler = true)),
                people = samplePeople,
            ),
            ScreenPreviewData(
                thumbnailUrl = "https://buly.kr/DEaVFRZ",
                title = "새로운 컬렉션",
                isBookmarked = false,
                authorNickname = "일반유저",
                authorUserRoleType = UserRoleType.FLING,
                contents = persistentListOf(sampleContent, sampleContent.copy(isSpoiler = true)),
                people = persistentListOf(),
            ),
        )
}

//@Preview
//@Composable
//private fun CollectionDetailScreenPreview(
//    @PreviewParameter(ScreenPreviewProvider::class) data: ScreenPreviewData,
//) {
//    FlintTheme {
//        Scaffold { paddingValues: PaddingValues ->
//            CollectionDetailScreen(
//                paddingValues = paddingValues,
//                title = data.title,
//                isBookmarked = data.isBookmarked,
//                authorNickname = data.authorNickname,
//                authorUserRoleType = data.authorUserRoleType,
//                createdAt = "2026. 01. 07.",
//                description = "시간이 흘러도 빛이 바래지 않는,\n사랑의 미묘한 온도를 담은 제 최애 영화 모음집입니다",
//                contents = data.contents,
//                people = data.people,
//                onSaveDoneButtonClick = {},
//                onSaveNoneButtonClick = {},
//                navigateUp = {},
//                onBookmarkIconClick = {},
//                onSpoilClick = {},
//                onAuthorClick = {},
//                onAuthorNicknameClick = {},
//                onAuthorClick = {},
//                targetImageUrl = {}
//            )
//        }
//    }
//}
