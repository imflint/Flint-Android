package com.flint.presentation.collectiondetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.common.util.UiState
import com.flint.core.designsystem.component.button.FlintSaveDoneButton
import com.flint.core.designsystem.component.button.FlintSaveNoneButton
import com.flint.core.designsystem.component.collection.PeopleBottomSheet
import com.flint.core.designsystem.component.collection.Spoiler
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.component.image.ProfileImage
import com.flint.core.designsystem.component.indicator.FlintLoadingIndicator
import com.flint.core.designsystem.component.progressbar.UnderImageProgressBar
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.bookmark.CollectionBookmarkUsersModel
import com.flint.domain.model.collection.CollectionDetailModelNew
import com.flint.domain.model.content.ContentModel
import com.flint.domain.model.content.ContentModelNew
import com.flint.domain.type.OttType
import com.flint.domain.type.UserRoleType
import com.flint.presentation.collectiondetail.uistate.CollectionDetailUiState
import com.flint.presentation.toast.ShowSaveToast
import com.flint.presentation.toast.ShowToast
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CollectionDetailRoute(
    paddingValues: PaddingValues,
    navigateToCollectionList: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: CollectionDetailViewModel = hiltViewModel(),
) {
    val uiState: UiState<CollectionDetailUiState> by viewModel.uiState.collectAsStateWithLifecycle()
    var showCancelToast: Boolean by remember { mutableStateOf(false) }
    var showSaveToast: Boolean by remember { mutableStateOf(false) }

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
                title = collectionDetail.title,
                authorId = collectionDetail.author.id,
                userId = collectionDetail.userId,
                isBookmarked = collectionDetail.isBookmarked,
                authorNickname = collectionDetail.author.nickname,
                authorUserRoleType = collectionDetail.author.userRole,
                createdAt = collectionDetail.createdAt,
                description = collectionDetail.description,
                contents = collectionDetail.contents,
                people = collectionBookmarkUsers,
                onSaveDoneButtonClick = viewModel::toggleCollectionBookmark,
                onSaveNoneButtonClick = viewModel::toggleCollectionBookmark,
                navigateUp = navigateUp
            )
        }

        else -> {}
    }

    if (showCancelToast) {
        ShowToast(
            text = "컬렉션 저장이 취소되었어요",
            imageVector = null,
            paddingValues = paddingValues,
            yOffset = 12.dp,
            hide = {
                showCancelToast = false
            }
        )
    }

    if (showSaveToast) {
        ShowSaveToast(
            navigateToSavedCollection = {
                navigateToCollectionList()
            },
            paddingValues = paddingValues,
            yOffset = 12.dp,
            hide = {
                showSaveToast = false
            })
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event: CollectionDetailEvent ->
            when (event) {
                CollectionDetailEvent.ToggleCollectionBookmarkFailure -> {
                    // TODO: 컬렉션 저장 실패 다이얼로그 띄우기
                }

                is CollectionDetailEvent.ToggleCollectionBookmarkSuccess -> {
                    if (event.isBookmarked) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionDetailScreen(
    paddingValues: PaddingValues,
    title: String,
    authorId: String,
    userId: String,
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
) {
    CompositionLocalProvider(
        LocalOverscrollFactory provides null,
    ) {
        var showPeopleBottomSheet: Boolean by remember { mutableStateOf(false) }
        val scrollState: ScrollState = rememberScrollState()
        var thumbnailHeight: Int by remember { mutableIntStateOf(0) }

        val scrollProgress: Float =
            if (scrollState.maxValue > 0) {
                scrollState.value.toFloat() / scrollState.maxValue
            } else {
                0f
            }

        val isProgressBarSticky: Boolean = scrollState.value >= thumbnailHeight

        if (showPeopleBottomSheet) {
            PeopleBottomSheet(
                people = people,
                onAuthorClick = { /* TODO: 프로필 화면으로 이동 */ },
                onDismiss = { showPeopleBottomSheet = false },
            )
        }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = FlintTheme.colors.background),
        ) {
            FlintBackTopAppbar(
                onClick = navigateUp,
                backgroundColor = Color.Transparent,
            )

            Box {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(bottom = 24.dp),
                ) {
                    Thumbnail(
                        title = title,
                        authorId = authorId,
                        userId = userId,
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
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                    )

                    Spacer(Modifier.height(48.dp))

                    contents.forEach { content: ContentModelNew ->
                        Content(
                            content = content,
                            onBookmarkIconClick = { contentId: String ->
                                // TODO: Content 저장
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

@Composable
private fun PeopleWhoSavedThisCollection(
    people: ImmutableList<CollectionBookmarkUsersModel.User>,
    onMoreClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(vertical = 10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "이 컬렉션을 저장한 사람들",
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.head2Sb20,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_more),
                contentDescription = null,
                modifier =
                    Modifier
                        .size(48.dp)
                        .clickable(onClick = onMoreClick)
                        .padding(12.dp),
                tint = FlintTheme.colors.white,
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy((-12).dp),
            ) {
                people.take(5).forEach { author: CollectionBookmarkUsersModel.User ->
                    ProfileImage(
                        imageUrl = author.profileImageUrl,
                        modifier =
                            Modifier
                                .size(56.dp)
                                .border(3.dp, FlintTheme.colors.background, CircleShape),
                        contentDescription = author.nickName,
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            if (people.size >= 6) {
                Row {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                        contentDescription = "그 외",
                        tint = FlintTheme.colors.white,
                    )

                    Text(
                        text = (people.size - 5).toString(),
                        color = FlintTheme.colors.gray50,
                        style = FlintTheme.typography.head2M20,
                    )
                }
            }
        }
    }
}

private class PeoplePreviewProvider :
    PreviewParameterProvider<ImmutableList<CollectionBookmarkUsersModel.User>> {
    override val values: Sequence<ImmutableList<CollectionBookmarkUsersModel.User>> =
        sequenceOf(
            persistentListOf(
                CollectionBookmarkUsersModel.User(
                    userId = "1",
                    nickName = "유저1",
                    profileImageUrl = "",
                    userRole = UserRoleType.FLING,
                ),
            ),
            persistentListOf(
                CollectionBookmarkUsersModel.User(
                    userId = "1",
                    nickName = "유저1",
                    profileImageUrl = "",
                    userRole = UserRoleType.ADMIN,
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
                CollectionBookmarkUsersModel.User(
                    userId = "4",
                    nickName = "유저4",
                    profileImageUrl = "",
                    userRole = UserRoleType.FLING,
                ),
                CollectionBookmarkUsersModel.User(
                    userId = "5",
                    nickName = "유저5",
                    profileImageUrl = "",
                    userRole = UserRoleType.FLING,
                ),
            ),
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
                    userRole = UserRoleType.FLING,
                ),
                CollectionBookmarkUsersModel.User(
                    userId = "3",
                    nickName = "유저3",
                    profileImageUrl = "",
                    userRole = UserRoleType.FLING,
                ),
                CollectionBookmarkUsersModel.User(
                    userId = "4",
                    nickName = "유저4",
                    profileImageUrl = "",
                    userRole = UserRoleType.FLING,
                ),
                CollectionBookmarkUsersModel.User(
                    userId = "5",
                    nickName = "유저5",
                    profileImageUrl = "",
                    userRole = UserRoleType.FLING,
                ),
                CollectionBookmarkUsersModel.User(
                    userId = "6",
                    nickName = "유저6",
                    profileImageUrl = "",
                    userRole = UserRoleType.FLINER,
                ),
                CollectionBookmarkUsersModel.User(
                    userId = "7",
                    nickName = "유저7",
                    profileImageUrl = "",
                    userRole = UserRoleType.ADMIN,
                ),
            ),
        )
}

@Preview
@Composable
private fun PeopleWhoSavedThisCollectionPreview(
    @PreviewParameter(PeoplePreviewProvider::class) people: ImmutableList<CollectionBookmarkUsersModel.User>,
) {
    FlintTheme {
        PeopleWhoSavedThisCollection(
            people = people,
            onMoreClick = {},
        )
    }
}

@Composable
private fun Thumbnail(
    title: String,
    authorId: String,
    userId: String,
    isBookmarked: Boolean,
    onSaveDoneButtonClick: () -> Unit,
    onSaveNoneButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(360f / 270f),
    ) {
        Image(
            painter = painterResource(R.drawable.img_collection_bg2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 55.dp, bottom = 19.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.display2M28,
                modifier = Modifier.fillMaxWidth(),
            )
            if (authorId != userId) {
                if (isBookmarked) {
                    FlintSaveDoneButton(
                        onClick = {
                            onSaveDoneButtonClick()
                        },
                    )
                } else {
                    FlintSaveNoneButton(
                        onClick = {
                            onSaveNoneButtonClick()
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun CollectionDetailDescription(
    authorNickname: String,
    authorUserRoleType: UserRoleType,
    createdAt: String,
    collectionContent: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = authorNickname,
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.head2Sb20,
            )

            if (authorUserRoleType == UserRoleType.FLINER) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_qualified),
                    contentDescription = "플리너",
                    tint = Color.Unspecified,
                )
            } else {
                Text(
                    "|",
                    color = FlintTheme.colors.gray200,
                    style = FlintTheme.typography.head3M18,
                )
            }

            Text(
                text = createdAt,
                color = FlintTheme.colors.gray200,
                style = FlintTheme.typography.body2M14,
            )
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .clip(CircleShape)
                    .background(color = FlintTheme.colors.gray300),
        )

        Text(
            text = collectionContent,
            color = FlintTheme.colors.gray100,
            style = FlintTheme.typography.body1R16,
        )
    }
}

@Composable
private fun Content(
    content: ContentModelNew,
    onBookmarkIconClick: (contentId: String) -> Unit,
) {
    Column {
        NetworkImage(
            imageUrl = content.imageUrl,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(360f / 480f),
            contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.height(32.dp))

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        content.title,
                        color = FlintTheme.colors.white,
                        style = FlintTheme.typography.head2Sb20,
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        content.year.toString(),
                        color = FlintTheme.colors.gray300,
                        style = FlintTheme.typography.body1R16,
                    )

                    Text(
                        content.director,
                        color = FlintTheme.colors.gray300,
                        style = FlintTheme.typography.body1R16,
                    )
                }

                Row(
                    modifier =
                        Modifier.noRippleClickable(onClick = { onBookmarkIconClick(content.id) }),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .padding(start = 24.dp)
                                .padding(vertical = 3.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        if (content.isBookmarked) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_bookmark_fill),
                                contentDescription = "저장됨",
                                tint = Color.Unspecified,
                            )
                        } else {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_bookmark_empty),
                                contentDescription = "저장되지 않음",
                                tint = Color.White,
                            )
                        }

                        Text(
                            text = content.bookmarkCount.toString(),
                            color = FlintTheme.colors.white,
                            style = FlintTheme.typography.caption1M12,
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(FlintTheme.colors.gray500),
            )

            Spacer(Modifier.height(24.dp))

            if (content.isSpoiler) {
                Spoiler(
                    spoil = {
                        // TODO: State 변경을 통해 isSpoiler 값 변경
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = content.reason,
                        color = FlintTheme.colors.gray100,
                        style = FlintTheme.typography.body1R16,
                        modifier =
                            Modifier
                                .defaultMinSize(minHeight = 183.dp)
                                .fillMaxWidth(),
                    )
                }
            } else {
                Text(
                    text = content.reason,
                    color = FlintTheme.colors.gray100,
                    style = FlintTheme.typography.body1R16,
                    modifier =
                        Modifier
                            .defaultMinSize(minHeight = 183.dp)
                            .fillMaxWidth(),
                )
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

private data class HeaderPreviewData(
    val title: String,
    val authorId: String,
    val userId: String,
    val isBookmarked: Boolean,
)

private class HeaderPreviewProvider : PreviewParameterProvider<HeaderPreviewData> {
    override val values: Sequence<HeaderPreviewData> =
        sequenceOf(
            HeaderPreviewData(
                title = "한번 보면 못 빠져나오는 여운남는 사랑이야기",
                authorId = "0",
                userId = "1",
                isBookmarked = true,
            ),
            HeaderPreviewData(
                title = "한번 보면 못 빠져나오는 여운남는 사랑이야기",
                authorId = "0",
                userId = "1",
                isBookmarked = false,
            ),
            HeaderPreviewData(
                title = "내가 만든 컬렉션",
                authorId = "1",
                userId = "1",
                isBookmarked = false,
            ),
        )
}

@Preview
@Composable
private fun ThumbnailPreview(
    @PreviewParameter(HeaderPreviewProvider::class) data: HeaderPreviewData,
) {
    FlintTheme {
        Thumbnail(
            title = data.title,
            authorId = data.authorId,
            userId = data.userId,
            isBookmarked = data.isBookmarked,
            onSaveDoneButtonClick = {},
            onSaveNoneButtonClick = {}
        )
    }
}

private data class DescriptionPreviewData(
    val authorNickname: String,
    val authorUserRoleType: UserRoleType,
    val createdAt: String,
    val collectionContent: String,
)

private class DescriptionPreviewProvider : PreviewParameterProvider<DescriptionPreviewData> {
    override val values: Sequence<DescriptionPreviewData> =
        sequenceOf(
            DescriptionPreviewData(
                authorNickname = "키카",
                authorUserRoleType = UserRoleType.FLINER,
                createdAt = "2026. 01. 07.",
                collectionContent = "시간이 흘러도 빛이 바래지 않는,\n사랑의 미묘한 온도를 담은 제 최애 영화 모음집입니다",
            ),
            DescriptionPreviewData(
                authorNickname = "일반유저",
                authorUserRoleType = UserRoleType.FLING,
                createdAt = "2026. 01. 15.",
                collectionContent = "제가 좋아하는 영화 모음입니다",
            ),
            DescriptionPreviewData(
                authorNickname = "관리자",
                authorUserRoleType = UserRoleType.ADMIN,
                createdAt = "2026. 01. 01.",
                collectionContent = "공식 추천 컬렉션입니다",
            ),
        )
}

@Preview
@Composable
private fun CollectionDetailDescriptionPreview(
    @PreviewParameter(DescriptionPreviewProvider::class) data: DescriptionPreviewData,
) {
    FlintTheme {
        CollectionDetailDescription(
            authorNickname = data.authorNickname,
            authorUserRoleType = data.authorUserRoleType,
            createdAt = data.createdAt,
            collectionContent = data.collectionContent,
        )
    }
}

private class ContentPreviewProvider : PreviewParameterProvider<ContentModel> {
    override val values: Sequence<ContentModel> =
        sequenceOf(
            ContentModel(
                contentId = "0",
                title = "드라마 제목",
                year = 2000,
                posterImage = "",
                ottSimpleList = listOf(OttType.Netflix, OttType.Disney),
                director = "가스 제닝스",
                description = "달라진 온도\n-\n같은 구도에 채도를 달리해 변해버린 사랑을 시각적으로 담아낸 장면들",
                isSpoiler = false,
                isBookmarked = false,
            ),
            ContentModel(
                contentId = "0",
                title = "스포일러 있는 영화",
                year = 2024,
                posterImage = "",
                ottSimpleList = listOf(OttType.Netflix),
                director = "감독 이름",
                description = "이 내용은 스포일러가 포함되어 있습니다.",
                isSpoiler = true,
                isBookmarked = false,
            ),
            ContentModel(
                contentId = "0",
                title = "저장된 영화",
                year = 2023,
                posterImage = "",
                ottSimpleList = listOf(OttType.Watcha, OttType.Wave),
                director = "다른 감독",
                description = "내가 저장한 영화입니다.",
                isSpoiler = false,
                isBookmarked = true,
                bookmarkCount = 42,
            ),
        )
}

@Preview
@Composable
private fun ContentPreview(
    @PreviewParameter(ContentPreviewProvider::class) content: ContentModelNew,
) {
    FlintTheme {
        Content(
            content = content,
            onBookmarkIconClick = {},
        )
    }
}

private data class ScreenPreviewData(
    val title: String,
    val authorId: String,
    val userId: String,
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
                title = "한번 보면 못 빠져나오는 여운남는 사랑이야기",
                authorId = "0",
                userId = "1",
                isBookmarked = true,
                authorNickname = "키카",
                authorUserRoleType = UserRoleType.FLINER,
                contents = persistentListOf(sampleContent, sampleContent.copy(isSpoiler = true)),
                people = samplePeople,
            ),
            ScreenPreviewData(
                title = "새로운 컬렉션",
                authorId = "0",
                userId = "1",
                isBookmarked = false,
                authorNickname = "일반유저",
                authorUserRoleType = UserRoleType.FLING,
                contents = persistentListOf(sampleContent, sampleContent.copy(isSpoiler = true)),
                people = persistentListOf(),
            ),
            ScreenPreviewData(
                title = "내가 만든 컬렉션",
                authorId = "1",
                userId = "1",
                isBookmarked = false,
                authorNickname = "나",
                authorUserRoleType = UserRoleType.FLING,
                contents = persistentListOf(sampleContent, sampleContent.copy(isSpoiler = true)),
                people = samplePeople,
            ),
        )
}

@Preview
@Composable
private fun CollectionDetailScreenPreview(
    @PreviewParameter(ScreenPreviewProvider::class) data: ScreenPreviewData,
) {
    FlintTheme {
        Scaffold { paddingValues: PaddingValues ->
            CollectionDetailScreen(
                paddingValues = paddingValues,
                title = data.title,
                authorId = data.authorId,
                userId = data.userId,
                isBookmarked = data.isBookmarked,
                authorNickname = data.authorNickname,
                authorUserRoleType = data.authorUserRoleType,
                createdAt = "2026. 01. 07.",
                description = "시간이 흘러도 빛이 바래지 않는,\n사랑의 미묘한 온도를 담은 제 최애 영화 모음집입니다",
                contents = data.contents,
                people = data.people,
                onSaveDoneButtonClick = {},
                onSaveNoneButtonClick = {},
                navigateUp = {}
            )
        }
    }
}
