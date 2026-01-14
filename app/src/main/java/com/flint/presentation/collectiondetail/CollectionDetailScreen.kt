package com.flint.presentation.collectiondetail

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintMediumButton
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
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = FlintTheme.colors.background),
    ) {
        CollectionDetailHeader(
            title = title,
            authorId = authorId,
            userId = userId,
            isBookmarked = isBookmarked,
        )

        LazyColumn(
            contentPadding = PaddingValues(vertical = 24.dp),
        ) {
            item {
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
            }

            item {
                Spacer(Modifier.height(48.dp))
            }

            items(contents) { content: ContentModel ->
                Content(
                    content = content,
                    onBookmarkIconClick = { contentId: Long ->
                        // TODO: Content 저장
                    },
                )
            }

            if (people.isNotEmpty()) {
                item {
                    PeopleWhoSavedThisCollection(people)
                }
            }
        }
    }
}

@Composable
private fun PeopleWhoSavedThisCollection(people: ImmutableList<AuthorModel>) {
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
                        .clickable(onClick = {})
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

@Preview
@Composable
private fun PeopleWhoSavedThisCollectionPreview() {
    FlintTheme {
        PeopleWhoSavedThisCollection(
            people =
                persistentListOf(
                    AuthorModel(
                        userId = 0,
                        nickname = "관리자",
                        profileUrl = "",
                        userRole = UserRoleType.ADMIN,
                    ),
                    AuthorModel(
                        userId = 0,
                        nickname = "플리너",
                        profileUrl = "",
                        userRole = UserRoleType.FLINER,
                    ),
                    AuthorModel(
                        userId = 0,
                        nickname = "플링",
                        profileUrl = "",
                        userRole = UserRoleType.FLING,
                    ),
                ),
        )
    }
}

@Preview
@Composable
private fun PeopleWhoSavedThisCollectionMoreThan5Preview() {
    FlintTheme {
        PeopleWhoSavedThisCollection(
            people =
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
}

@Composable
private fun CollectionDetailHeader(
    title: String,
    authorId: Long,
    userId: Long,
    isBookmarked: Boolean,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(360f / 270f),
    ) {
        Image(
            painter = painterResource(R.drawable.img_collection_bg2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        Column(modifier = Modifier.fillMaxSize()) {
            FlintBackTopAppbar(
                onClick = { },
                backgroundColor = Color.Transparent,
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 19.dp),
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

            UnderImageProgressBar(
                progress = 0.5f, // TODO: Progress 퍼센티지 추가
            )
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

@Preview
@Composable
private fun CollectionDetailHeaderPreview() {
    FlintTheme {
        CollectionDetailHeader(
            title = "한번 보면 못 빠져나오는 여운남는 사랑이야기",
            authorId = 0L,
            userId = 1L,
            isBookmarked = true,
        )
    }
}

@Preview
@Composable
private fun CollectionDetailDescriptionPreview() {
    FlintTheme {
        CollectionDetailDescription(
            authorNickname = "키카",
            authorUserRoleType = UserRoleType.FLINER,
            createdAt = "2026. 01. 07.",
            collectionContent = "시간이 흘러도 빛이 바래지 않는,\n사랑의 미묘한 온도를 담은 제 최애 영화 모음집입니다",
        )
    }
}

@Preview
@Composable
private fun ContentPreview() {
    FlintTheme {
        Content(
            content =
                ContentModel(
                    contentId = 0,
                    title = "드라마 제목",
                    year = 2000,
                    posterImage = "",
                    ottSimpleList =
                        listOf(
                            OttType.Netflix,
                            OttType.Disney,
                            OttType.Tving,
                            OttType.Coupang,
                            OttType.Wave,
                            OttType.Watcha,
                        ),
                    director = "가스 제닝스",
                    description =
                        """
                        달라진 온도
                        -
                        같은 구도에 채도를 달리해 변해버린 사랑을 시각적
                        으로 담아낸 장면들
                        
                        어떻게 이런 연출을 할 수 있는지 감독이 너무
                        변태같다
                        """.trimIndent(),
                ),
            onBookmarkIconClick = {},
        )
    }
}

@Preview
@Composable
private fun CollectionDetailScreenPreview() {
    FlintTheme {
        Scaffold { paddingValues: PaddingValues ->
            CollectionDetailScreen(
                paddingValues,
                {},
                title = "한번 보면 못 빠져나오는 여운남는 사랑이야기",
                authorId = 0L,
                userId = 1L,
                isBookmarked = true,
                authorNickname = "키카",
                authorUserRoleType = UserRoleType.FLINER,
                createdAt = "2026. 01. 07.",
                collectionContent = "시간이 흘러도 빛이 바래지 않는,\n사랑의 미묘한 온도를 담은 제 최애 영화 모음집입니다",
                contents =
                    persistentListOf(
                        ContentModel(
                            contentId = 0,
                            title = "드라마 제목",
                            year = 2000,
                            posterImage = "",
                            ottSimpleList =
                                listOf(
                                    OttType.Netflix,
                                    OttType.Disney,
                                    OttType.Tving,
                                    OttType.Coupang,
                                    OttType.Wave,
                                    OttType.Watcha,
                                ),
                            director = "감독 이름",
                            description =
                                """
                                달라진 온도
                                -
                                같은 구도에 채도를 달리해 변해버린 사랑을 시각적
                                으로 담아낸 장면들
                                
                                어떻게 이런 연출을 할 수 있는지 감독이 너무
                                변태같다
                                """.trimIndent(),
                            isSpoiler = true,
                        ),
                        ContentModel(
                            contentId = 0,
                            title = "드라마 제목",
                            year = 2000,
                            posterImage = "",
                            ottSimpleList =
                                listOf(
                                    OttType.Netflix,
                                    OttType.Disney,
                                    OttType.Tving,
                                    OttType.Coupang,
                                    OttType.Wave,
                                    OttType.Watcha,
                                ),
                            director = "감독 이름",
                            description =
                                """
                                난 비록 한 줄이지만 height은 최소 183이라는 말씀
                                """.trimIndent(),
                        ),
                    ),
                people =
                    persistentListOf(
                        AuthorModel(
                            userId = 0,
                            nickname = "관리자",
                            profileUrl = "",
                            userRole = UserRoleType.ADMIN,
                        ),
                        AuthorModel(
                            userId = 0,
                            nickname = "플리너",
                            profileUrl = "",
                            userRole = UserRoleType.FLINER,
                        ),
                        AuthorModel(
                            userId = 0,
                            nickname = "플링",
                            profileUrl = "",
                            userRole = UserRoleType.FLING,
                        ),
                    ),
            )
        }
    }
}
