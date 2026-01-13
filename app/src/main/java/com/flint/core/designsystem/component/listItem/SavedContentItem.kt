package com.flint.core.designsystem.component.listItem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.component.listView.OttHorizontalList
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.ContentModel
import com.flint.domain.type.OttType

@Composable
fun SavedContentItem(
    contentModel: ContentModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .width(120.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(180.dp),
        ) {
            NetworkImage(
                imageUrl = contentModel.posterImage,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxSize(),
            )

            OttHorizontalList(
                ottList = contentModel.ottSimpleList,
                modifier =
                    Modifier
                        .padding(top = 10.dp, start = 8.dp),
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = contentModel.title,
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.body1M16,
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "${contentModel.year}년도",
            color = FlintTheme.colors.gray300,
            style = FlintTheme.typography.caption1R12,
        )
    }
}

@Preview
@Composable
private fun PreviewSavedContentItem() {
    FlintTheme {
        val contentModel =
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
            )

        SavedContentItem(
            contentModel = contentModel,
        )
    }
}
