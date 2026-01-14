package com.flint.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.component.listItem.CollectionItem
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.AuthorModel
import com.flint.domain.model.CollectionModel
import com.flint.domain.type.UserRoleType

@Composable
fun HomeRecentCollection(
    collectionModelList: List<CollectionModel>,
    onItemClick: (id: String) -> Unit,
    onAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "눈여겨보고 있는 컬렉션",
                    style = FlintTheme.typography.head3Sb18,
                    color = FlintTheme.colors.white,
                    modifier = Modifier.padding(start = 16.dp),
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "키카님이 최근 살펴본 컬렉션이에요",
                    style = FlintTheme.typography.body2R14,
                    color = FlintTheme.colors.white,
                    modifier = Modifier.padding(start = 16.dp),
                )
            }

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_more),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier =
                    Modifier
                        .size(24.dp)
                        .noRippleClickable {
                            onAllClick()
                        },
            )
        }

        Spacer(Modifier.height(24.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            itemsIndexed(collectionModelList) { _, item ->
                CollectionItem(
                    collectionModel = item,
                    onItemClick = { id ->
                        onItemClick(id)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewHomeRecentCollection() {
    FlintTheme {
        val collectionModelList =
            listOf(
                CollectionModel(
                    collectionId = "",
                    collectionTitle = "컬렉션 제목",
                    collectionImageUrl = "",
                    createdAt = "",
                    isBookmarked = false,
                    author =
                        AuthorModel(
                            userId = 0,
                            nickname = "사용자 이름",
                            profileUrl = "",
                            userRole = UserRoleType.FLINER,
                        ),
                ),
                CollectionModel(
                    collectionId = "",
                    collectionTitle = "컬렉션 제목2",
                    collectionImageUrl = "",
                    createdAt = "",
                    isBookmarked = false,
                    author =
                        AuthorModel(
                            userId = 0,
                            nickname = "사용자 이름2",
                            profileUrl = "",
                            userRole = UserRoleType.FLINER,
                        ),
                ),
            )

        HomeRecentCollection(
            collectionModelList = collectionModelList,
            onItemClick = { },
            onAllClick = { },
        )
    }
}
