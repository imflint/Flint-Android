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
import com.flint.core.designsystem.component.listItem.SavedContentItem
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.ContentModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SavedContentsSection(
    title: String,
    description: String,
    isAllVisible: Boolean,
    onAllClick: () -> Unit,
    contentModelList: ImmutableList<ContentModel>,
    onItemClick: (contentId: Long) -> Unit,
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
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = title,
                    style = FlintTheme.typography.head3Sb18,
                    color = FlintTheme.colors.white,
                    modifier = Modifier.padding(start = 16.dp),
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = description,
                    style = FlintTheme.typography.body2R14,
                    color = FlintTheme.colors.gray100,
                    modifier = Modifier.padding(start = 16.dp),
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
            itemsIndexed(contentModelList) { _, item ->
                SavedContentItem(
                    contentModel = item,
                    onItemClick = { contentId ->
                        onItemClick(contentId)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewHomeSavedContents() {
    FlintTheme {
        val contentModelList = ContentModel.FakeList

        SavedContentsSection(
            title = "최근 저장한 콘텐츠",
            description = "현재 구독 중인 OTT에서 볼 수 있는 작품들이에요",
            isAllVisible = true,
            contentModelList = contentModelList,
            onItemClick = {},
            onAllClick = {},
        )
    }
}
