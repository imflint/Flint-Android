package com.flint.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.listItem.SavedContentItem
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.ContentModel
import com.flint.domain.type.OttType

@Composable
fun HomeSavedContents(
    contentModelList: List<ContentModel>,
    onItemClick: (contentId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Text(
            text = "최근 저장한 콘텐츠 ",
            style = FlintTheme.typography.head3Sb18,
            color = FlintTheme.colors.white,
            modifier = Modifier.padding(start = 16.dp),
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "현재 구독 중인 OTT에서 볼 수 있는 작품들이에요",
            style = FlintTheme.typography.body2R14,
            color = FlintTheme.colors.white,
            modifier = Modifier.padding(start = 16.dp),
        )

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
        val contentModelList =
            listOf(
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
                        ),
                ),
                ContentModel(
                    contentId = 0,
                    title = "드라마 제목2",
                    year = 2020,
                    posterImage = "",
                    ottSimpleList =
                        listOf(
                            OttType.Wave,
                            OttType.Watcha,
                            OttType.Tving,
                        ),
                ),
                ContentModel(
                    contentId = 0,
                    title = "드라마 제목3",
                    year = 2003,
                    posterImage = "",
                    ottSimpleList =
                        listOf(
                            OttType.Disney,
                            OttType.Tving,
                        ),
                ),
                ContentModel(
                    contentId = 0,
                    title = "드라마 제목4",
                    year = 1919,
                    posterImage = "",
                    ottSimpleList =
                        listOf(
                            OttType.Watcha,
                        ),
                ),
            )

        HomeSavedContents(
            contentModelList = contentModelList,
            onItemClick = {},
        )
    }
}
