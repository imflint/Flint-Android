@file:OptIn(ExperimentalMaterial3Api::class)

package com.flint.core.designsystem.component.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.component.bottomsheet.FlintBasicBottomSheet
import com.flint.core.designsystem.component.image.ProfileImage
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.user.AuthorModel
import com.flint.domain.type.UserRoleType

@Composable
fun PeopleBottomSheet(
    people: List<AuthorModel>,
    onAuthorClick: (userId: String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    FlintBasicBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 543.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "이 컬렉션을 저장한 사람들",
                    color = FlintTheme.colors.white,
                    style = FlintTheme.typography.head3Sb18,
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = "${people.size}",
                    color = FlintTheme.colors.white,
                    style = FlintTheme.typography.head3M18,
                )
            }

            Spacer(Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(people) { author: AuthorModel ->
                    Author(
                        author = author,
                        onClick = onAuthorClick,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                    )
                }

                item {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun Author(
    author: AuthorModel,
    onClick: (userId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .defaultMinSize(minHeight = 48.dp)
                .clickable(onClick = { onClick(author.userId) }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileImage(
            imageUrl = author.profileUrl,
            modifier = Modifier.size(44.dp),
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = author.nickname,
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.body1Sb16,
        )

        if (author.userRole == UserRoleType.FLINER) {
            Spacer(Modifier.width(10.dp))

            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_qualified),
                contentDescription = "플리너",
            )
        }
    }
}

@Preview
@Composable
private fun PeopleBottomSheetPreview(
    @PreviewParameter(PeopleBottomSheetPreviewParameterProvider::class) people: List<AuthorModel>,
) {
    var showBottomSheet by remember { mutableStateOf(true) }
    FlintTheme {
        if (showBottomSheet) {
            PeopleBottomSheet(
                people = people,
                onAuthorClick = {},
                onDismiss = { showBottomSheet = !showBottomSheet },
            )
        }
    }
}

@Preview
@Composable
private fun AuthorPreview(
    @PreviewParameter(AuthorPreviewParameterProvider::class) author: AuthorModel,
) {
    FlintTheme {
        Author(
            author = author,
            onClick = {},
        )
    }
}

private class PeopleBottomSheetPreviewParameterProvider : PreviewParameterProvider<List<AuthorModel>> {
    override val values: Sequence<List<AuthorModel>> =
        sequenceOf(
            sampleAuthors,
            List(4) { sampleAuthors }.flatten(),
        )
}

private class AuthorPreviewParameterProvider : PreviewParameterProvider<AuthorModel> {
    override val values: Sequence<AuthorModel> = sampleAuthors.asSequence()
}

private val sampleAuthors: List<AuthorModel> =
    listOf(
        AuthorModel(
            userId = "0",
            nickname = "관리자",
            profileUrl = "",
            userRole = UserRoleType.ADMIN,
        ),
        AuthorModel(
            userId = "0",
            nickname = "플리너",
            profileUrl = "",
            userRole = UserRoleType.FLINER,
        ),
        AuthorModel(
            userId = "0",
            nickname = "플링",
            profileUrl = "",
            userRole = UserRoleType.FLING,
        ),
    )
