package com.flint.android.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.flint.android.R
import com.flint.android.core.designsystem.component.image.NetworkImage
import com.flint.android.core.designsystem.component.listView.OttHorizontalList
import com.flint.android.core.designsystem.interaction.flintCardClickable
import com.flint.android.core.designsystem.interaction.flintIconClickable
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.type.OttType

@Composable
fun CollectionCreateContentBookmark(
    onBookmarkClick: () -> Unit,
    onMoreClick: () -> Unit,
    isBookmarked: Boolean,
    bookmarkCount: Int,
    imageUrl: String,
    title: String,
    director: String,
    createdYear: Int,
    ottList: List<OttType>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        CollectionCreateContentBookmarkImage(
            imageUrl = imageUrl,
            ottList = ottList,
            modifier =
                Modifier
                    .height(150.dp)
                    .width(100.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        CollectionCreateContentBookmarkInfo(
            title = title,
            director = director,
            createdYear = createdYear,
            onMoreClick = onMoreClick,
            modifier =
                Modifier
                    .weight(1f)
                    .height(150.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        CollectionCreateContentBookmarkTag(
            isBookmarked = isBookmarked,
            bookmarkCount = bookmarkCount,
            onClick = onBookmarkClick,
        )
    }
}

@Composable
private fun CollectionCreateContentBookmarkImage(
    imageUrl: String,
    ottList: List<OttType>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        NetworkImage(
            imageUrl = imageUrl,
            modifier = Modifier.fillMaxWidth().height(150.dp),
        )

        OttHorizontalList(
            ottList = ottList,
            modifier =
                Modifier
                    .padding(top = 10.dp, start = 8.dp),
        )
    }
}

@Composable
private fun CollectionCreateContentBookmarkInfo(
    title: String,
    director: String,
    createdYear: Int,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
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
            text = createdYear.toString(),
            modifier = Modifier.fillMaxWidth(),
            color = FlintTheme.colors.gray300,
            style = FlintTheme.typography.caption1M12,
        )

        Spacer(modifier = Modifier.weight(1f))

        CollectionCreateContentBookmarkMore(
            onClick = onMoreClick,
        )
    }
}

@Composable
private fun CollectionCreateContentBookmarkTag(
    isBookmarked: Boolean,
    bookmarkCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector =
                ImageVector.vectorResource(
                    if (isBookmarked) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_empty,
                ),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier =
                Modifier
                    .size(24.dp)
                    .flintIconClickable(onClick = onClick),
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = bookmarkCount.toString(),
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.caption1M12,
        )
    }
}

@Composable
private fun CollectionCreateContentBookmarkMore(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.flintCardClickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "시청 가능한 OTT",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.body2R14,
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_more),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(16.dp),
        )
    }
}




@Preview
@Composable
private fun CollectionCreateContentBookmarkPreview() {
    FlintTheme {
        var isBookmarked by remember { mutableStateOf(true) }
        CollectionCreateContentBookmark(
            onBookmarkClick = { isBookmarked = !isBookmarked },
            onMoreClick = {},
            isBookmarked = isBookmarked,
            bookmarkCount = 413,
            imageUrl = "https://buly.kr/DEaVFRZ",
            title = "어바웃 타임",
            director = "스파이더맨",
            createdYear = 2001,
            ottList =
                listOf(
                    OttType.Netflix,
                    OttType.Disney,
                    OttType.Tving,
                    OttType.CoupangPlay,
                ),
        )
    }
}
