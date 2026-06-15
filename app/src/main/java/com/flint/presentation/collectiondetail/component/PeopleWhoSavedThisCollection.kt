package com.flint.presentation.collectiondetail.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.component.image.ProfileImage
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.bookmark.CollectionBookmarkUsersModel
import com.flint.domain.type.UserRoleType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PeopleWhoSavedThisCollection(
    people: ImmutableList<CollectionBookmarkUsersModel.User>,
    onMoreClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 32.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "이 컬렉션을 저장한 사람들",
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.head2Sb20,
                modifier = Modifier,
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_more),
                contentDescription = null,
                modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onMoreClick)
                        .padding(vertical = 1.dp)
                        .padding(end = 3.dp),
                tint = FlintTheme.colors.white,
            )
        }

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy((-12).dp),
            ) {
                people.take(6).forEach { author: CollectionBookmarkUsersModel.User ->
                    ProfileImage(
                        imageUrl = author.profileImageUrl,
                        modifier = Modifier
                                .size(56.dp)
                                .border(3.dp, FlintTheme.colors.background, CircleShape),
                        contentDescription = author.nickName,
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            if (people.size >= 7) {
                Row {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                        contentDescription = "그 외",
                        tint = FlintTheme.colors.white,
                    )

                    Text(
                        text = (people.size - 6).toString(),
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
