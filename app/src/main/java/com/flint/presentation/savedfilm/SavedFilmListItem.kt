package com.flint.presentation.savedfilm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.component.listView.OttHorizontalList
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.ContentModel
import com.flint.domain.type.OttType

@Composable
fun SavedFilmListItem(
    contentModel: ContentModel,
    title: String,
    director: String,
    createdYear: String,
    isBookmarked: Boolean,
    bookmarkCount: Int,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(color = FlintTheme.colors.background),
    ) {
        SavedFilmListItemImage(
            contentModel = contentModel,
            modifier = Modifier.fillMaxHeight(),
        )

        Spacer(modifier = Modifier.width(12.dp))

        SavedFilmListItemInfo(
            title = title,
            director = director,
            createdYear = createdYear,
            modifier =
                Modifier
                    .fillMaxHeight()
                    .weight(1f),
        )

        Spacer(modifier = Modifier.width(4.dp))

        SavedFilmListItemBookmark(
            isBookmarked = isBookmarked,
            bookmarkCount = bookmarkCount,
            onBookmarkClick = onBookmarkClick,
        )
    }
}

@Composable
private fun SavedFilmListItemImage(
    contentModel: ContentModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(100.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            NetworkImage(
                imageUrl = contentModel.posterImage,
                modifier = Modifier.fillMaxSize(),
            )

            OttHorizontalList(
                ottList = contentModel.ottSimpleList,
                modifier =
                    Modifier
                        .padding(top = 10.dp, start = 8.dp),
            )
        }
    }
}

@Composable
private fun SavedFilmListItemInfo(
    title: String,
    director: String,
    createdYear: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                color = FlintTheme.colors.white,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = FlintTheme.typography.body1M16,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = director,
                modifier = Modifier.fillMaxWidth(),
                color = FlintTheme.colors.gray300,
                style = FlintTheme.typography.caption1M12,
            )

            Text(
                text = createdYear,
                modifier = Modifier.fillMaxWidth(),
                color = FlintTheme.colors.gray300,
                style = FlintTheme.typography.caption1M12,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "작품 보러가기",
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.body2R14,
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_more),
                modifier = Modifier.size(16.dp),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    }
}

@Composable
private fun SavedFilmListItemBookmark(
    isBookmarked: Boolean,
    bookmarkCount: Int,
    onBookmarkClick: () -> Unit,
) {
    Column(
        modifier = Modifier.size(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(if (isBookmarked) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_empty),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier =
                Modifier
                    .size(24.dp)
                    .clickable(onClick = onBookmarkClick),
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "$bookmarkCount",
            color = FlintTheme.colors.gray50,
            style = FlintTheme.typography.caption1M12,
        )
    }
}

@Preview
@Composable
private fun SavedFilmListItemBookmarkPreview() {
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
        var isBookmarked by remember { mutableStateOf(false) }

        SavedFilmListItem(
            contentModel = contentModel,
            title = "해리포터 불의 잔 해리포터 불의 잔 해리포터 불의 잔 해리포터 불의 잔 해리포터 불의 잔 해리포터 불의 잔",
            director = "메롱",
            createdYear = "2005",
            isBookmarked = isBookmarked,
            bookmarkCount = 128,
            onBookmarkClick = { isBookmarked = !isBookmarked },
        )
    }
}
