package com.flint.core.designsystem.component.listView

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
import com.flint.data.model.collection.CollectionModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CollectionSection(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    isAllVisible: Boolean,
    onAllClick: () -> Unit,
    collectionModelList: ImmutableList<CollectionModel>,
    onItemClick: (id: String) -> Unit,
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
                    .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = title,
                    style = FlintTheme.typography.head3Sb18,
                    color = FlintTheme.colors.white,
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = description,
                    style = FlintTheme.typography.body2R14,
                    color = FlintTheme.colors.gray100,
                )
            }

            Spacer(Modifier.weight(1f))

            if (isAllVisible) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_more),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier =
                        Modifier
                            .size(48.dp)
                            .noRippleClickable {
                                onAllClick()
                            }.padding(12.dp),
                )
            }
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

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun CollectionSectionPreview() {
    FlintTheme {
        val collectionModelList = CollectionModel.FakeList

        CollectionSection(
            collectionModelList = collectionModelList,
            title = "저장한 컬렉션",
            description = "키카님이 저장한 작품이에요.",
            onAllClick = {},
            onItemClick = {},
            isAllVisible = true,
        )
    }
}
