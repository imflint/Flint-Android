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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintMediumButton
import com.flint.core.designsystem.component.collection.PeopleBottomSheet
import com.flint.core.designsystem.component.collection.Spoiler
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.component.image.ProfileImage
import com.flint.core.designsystem.component.progressbar.UnderImageProgressBar
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.AuthorModel
import com.flint.domain.model.ContentModel
import com.flint.domain.type.OttType
import com.flint.domain.type.UserRoleType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CollectionDetailRoute(
    paddingValues: PaddingValues,
    collectionId: String,
    navigateToCollectionList: () -> Unit,
) {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionDetailScreen(
    paddingValues: PaddingValues,
    navigateToCollectionList: () -> Unit,
    title: String,
    authorId: Long,
    userId: Long,
    isBookmarked: Boolean,
    authorNickname: String,
    authorUserRoleType: UserRoleType,
    createdAt: String,
    collectionContent: String,
    contents: ImmutableList<ContentModel>,
    people: ImmutableList<AuthorModel>,
) {
    CompositionLocalProvider(
        LocalOverscrollFactory provides null,
    ) {
        var showPeopleBottomSheet: Boolean by remember { mutableStateOf(false) }
        val scrollState: ScrollState = rememberScrollState()
        var thumbnailHeight: Int by remember { mutableIntStateOf(0) }
        val topPaddingPx: Int = with(LocalDensity.current) { 24.dp.toPx().toInt() }

        val scrollProgress: Float =
            if (scrollState.maxValue > 0) {
                scrollState.value.toFloat() / scrollState.maxValue
            } else {
                0f
            }

        // Thumbnail + 상단 패딩(24dp)이 스크롤되어 사라지면 sticky
        val isProgressBarSticky = scrollState.value >= thumbnailHeight + topPaddingPx

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
                onClick = { },
                backgroundColor = Color.Transparent,
            )

            Box {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(vertical = 24.dp),
                ) {
                    Thumbnail(
                        title = title,
                        authorId = authorId,
                        userId = userId,
                        isBookmarked = isBookmarked,
                        modifier =
                            Modifier.onGloballyPositioned { coordinates: LayoutCoordinates ->
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
                        collectionContent = collectionContent,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                    )

                    Spacer(Modifier.height(48.dp))

                    contents.forEach { content: ContentModel ->
                        Content(
                            content = content,
                            onBookmarkIconClick = { contentId: Long ->
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
    people: ImmutableList<AuthorModel>,
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
                people.take(5).forEach { author: AuthorModel ->
                    ProfileImage(
                        imageUrl = author.profileUrl,
                        modifier =
                            Modifier
                                .size(56.dp)
                                .border(3.dp, FlintTheme.colors.background, CircleShape),
                        contentDescription = author.nickname,
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

private class PeoplePreviewProvider : PreviewParameterProvider<ImmutableList<AuthorModel>> {
    override val values: Sequence<ImmutableList<AuthorModel>> =
        sequenceOf(
            persistentListOf(
                AuthorModel(
                    userId = 1,
                    nickname = "유저1",
                    profileUrl = "",
                    userRole = UserRoleType.FLING,
                ),
            ),
            persistentListOf(
                AuthorModel(
                    userId = 1,
                    nickname = "유저1",
                    profileUrl = "",
                    userRole = UserRoleType.ADMIN,
                ),
                AuthorModel(
                    userId = 2,
                    nickname = "유저2",
                    profileUrl = "",
                    userRole = UserRoleType.FLINER,
                ),
                AuthorModel(
                    userId = 3,
                    nickname = "유저3",
                    profileUrl = "",
                    userRole = UserRoleType.FLING,
                ),
                AuthorModel(
                    userId = 4,
                    nickname = "유저4",
                    profileUrl = "",
                    userRole = UserRoleType.FLING,
                ),
                AuthorModel(
                    userId = 5,
                    nickname = "유저5",
                    profileUrl = "",
                    userRole = UserRoleType.FLING,
                ),
            ),
            persistentListOf(
                AuthorModel(
                    userId = 1,
                    nickname = "유저1",
                    profileUrl = "",
                    userRole = UserRoleType.FLING,
                ),
                AuthorModel(
                    userId = 2,
                    nickname = "유저2",
                    profileUrl = "",
                    userRole = UserRoleType.FLING,
                ),
                AuthorModel(
                    userId = 3,
                    nickname = "유저3",
                    profileUrl = "",
                    userRole = UserRoleType.FLING,
                ),
                AuthorModel(
                    userId = 4,
                    nickname = "유저4",
                    profileUrl = "",
                    userRole = UserRoleType.FLING,
                ),
                AuthorModel(
                    userId = 5,
                    nickname = "유저5",
                    profileUrl = "",
                    userRole = UserRoleType.FLING,
                ),
                AuthorModel(
                    userId = 6,
                    nickname = "유저6",
                    profileUrl = "",
                    userRole = UserRoleType.FLINER,
                ),
                AuthorModel(
                    userId = 7,
                    nickname = "유저7",
                    profileUrl = "",
                    userRole = UserRoleType.ADMIN,
                ),
            ),
        )
}

@Preview
@Composable
private fun PeopleWhoSavedThisCollectionPreview(
    @PreviewParameter(PeoplePreviewProvider::class) people: ImmutableList<AuthorModel>,
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
    authorId: Long,
    userId: Long,
    isBookmarked: Boolean,
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
                    FlintMediumButton(
                        text = "저장된 컬렉션",
                        state = FlintButtonState.Able,
                        onClick = {
                            // TODO: 저장된 컬렉션 해제
                        },
                    )
                } else {
                    FlintMediumButton(
                        text = "컬렉션 저장 +",
                        state = FlintButtonState.Outline,
                        onClick = {
                            // TODO: 컬렉션 저장
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
    content: ContentModel,
    onBookmarkIconClick: (contentId: Long) -> Unit,
) {
    Column {
        NetworkImage(
            imageUrl = content.posterImage,
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
                        Modifier.noRippleClickable(onClick = { onBookmarkIconClick(content.contentId) }),
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
                        text = content.description,
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
                    text = content.description,
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
    val authorId: Long,
    val userId: Long,
    val isBookmarked: Boolean,
)

private class HeaderPreviewProvider : PreviewParameterProvider<HeaderPreviewData> {
    override val values: Sequence<HeaderPreviewData> =
        sequenceOf(
            HeaderPreviewData(
                title = "한번 보면 못 빠져나오는 여운남는 사랑이야기",
                authorId = 0L,
                userId = 1L,
                isBookmarked = true,
            ),
            HeaderPreviewData(
                title = "한번 보면 못 빠져나오는 여운남는 사랑이야기",
                authorId = 0L,
                userId = 1L,
                isBookmarked = false,
            ),
            HeaderPreviewData(
                title = "내가 만든 컬렉션",
                authorId = 1L,
                userId = 1L,
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
                contentId = 0,
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
                contentId = 1,
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
                contentId = 2,
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
    @PreviewParameter(ContentPreviewProvider::class) content: ContentModel,
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
    val authorId: Long,
    val userId: Long,
    val isBookmarked: Boolean,
    val authorNickname: String,
    val authorUserRoleType: UserRoleType,
    val contents: ImmutableList<ContentModel>,
    val people: ImmutableList<AuthorModel>,
)

private class ScreenPreviewProvider : PreviewParameterProvider<ScreenPreviewData> {
    private val sampleContent =
        ContentModel(
            contentId = 0,
            title = "드라마 제목",
            year = 2000,
            posterImage = "",
            ottSimpleList = listOf(OttType.Netflix, OttType.Disney),
            director = "감독 이름",
            description = "달라진 온도\n-\n같은 구도에 채도를 달리해 변해버린 사랑을 시각적으로 담아낸 장면들",
        )

    private val samplePeople =
        persistentListOf(
            AuthorModel(
                userId = 1,
                nickname = "유저1",
                profileUrl = "",
                userRole = UserRoleType.FLING,
            ),
            AuthorModel(
                userId = 2,
                nickname = "유저2",
                profileUrl = "",
                userRole = UserRoleType.FLINER,
            ),
            AuthorModel(
                userId = 3,
                nickname = "유저3",
                profileUrl = "",
                userRole = UserRoleType.FLING,
            ),
        )

    override val values: Sequence<ScreenPreviewData> =
        sequenceOf(
            ScreenPreviewData(
                title = "한번 보면 못 빠져나오는 여운남는 사랑이야기",
                authorId = 0L,
                userId = 1L,
                isBookmarked = true,
                authorNickname = "키카",
                authorUserRoleType = UserRoleType.FLINER,
                contents = persistentListOf(sampleContent),
                people = samplePeople,
            ),
            ScreenPreviewData(
                title = "새로운 컬렉션",
                authorId = 0L,
                userId = 1L,
                isBookmarked = false,
                authorNickname = "일반유저",
                authorUserRoleType = UserRoleType.FLING,
                contents = persistentListOf(sampleContent),
                people = persistentListOf(),
            ),
            ScreenPreviewData(
                title = "내가 만든 컬렉션",
                authorId = 1L,
                userId = 1L,
                isBookmarked = false,
                authorNickname = "나",
                authorUserRoleType = UserRoleType.FLING,
                contents = persistentListOf(sampleContent),
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
                navigateToCollectionList = {},
                title = data.title,
                authorId = data.authorId,
                userId = data.userId,
                isBookmarked = data.isBookmarked,
                authorNickname = data.authorNickname,
                authorUserRoleType = data.authorUserRoleType,
                createdAt = "2026. 01. 07.",
                collectionContent = "시간이 흘러도 빛이 바래지 않는,\n사랑의 미묘한 온도를 담은 제 최애 영화 모음집입니다",
                contents = data.contents,
                people = data.people,
            )
        }
    }
}
