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
import com.flint.android.core.common.extension.toDisplayYear
import com.flint.android.core.designsystem.component.image.NetworkImage
import com.flint.android.core.designsystem.interaction.flintIconClickable
import com.flint.android.core.designsystem.theme.FlintTheme

@Composable
fun CollectionCreateContentBookmark(
    onBookmarkClick: () -> Unit,
    isBookmarked: Boolean,
    bookmarkCount: Int,
    imageUrl: String,
    title: String,
    /** 감독(영화) 또는 creator(TV). 서버가 내려주지 않으면 null 이고, 이때는 영역 자체를 그리지 않는다. */
    director: String?,
    createdYear: Int,
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
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        NetworkImage(
            imageUrl = imageUrl,
            modifier = Modifier.fillMaxWidth().height(150.dp),
        )
    }
}

@Composable
private fun CollectionCreateContentBookmarkInfo(
    title: String,
    director: String?,
    createdYear: Int,
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

        if (!director.isNullOrBlank()) {
            Text(
                text = director,
                modifier = Modifier.fillMaxWidth(),
                color = FlintTheme.colors.gray300,
                style = FlintTheme.typography.caption1M12,
            )
        }

        createdYear.toDisplayYear()?.let { year ->
            Text(
                text = year,
                modifier = Modifier.fillMaxWidth(),
                color = FlintTheme.colors.gray300,
                style = FlintTheme.typography.caption1M12,
            )
        }
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

/** 서버가 감독을 내려주지 않는 경우 — 감독 줄 없이 연도가 바로 붙는다. */
@Preview
@Composable
private fun CollectionCreateContentBookmarkNoDirectorPreview() {
    FlintTheme {
        CollectionCreateContentBookmark(
            onBookmarkClick = {},
            isBookmarked = true,
            bookmarkCount = 60,
            imageUrl = "",
            title = "너에게 닿기를",
            director = null,
            createdYear = 2009,
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
            isBookmarked = isBookmarked,
            bookmarkCount = 413,
            imageUrl = "https://buly.kr/DEaVFRZ",
            title = "어바웃 타임",
            director = "스파이더맨",
            createdYear = 2001,
        )
    }
}
