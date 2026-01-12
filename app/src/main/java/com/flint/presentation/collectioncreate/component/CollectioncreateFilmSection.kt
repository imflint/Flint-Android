package com.flint.presentation.collectioncreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionCreateFilmSection(
    imageUrl: String,
    title: String,
    director: String,
    createdYear: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.background(color = FlintTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TO-DO 이미지 URL

        Box(
            modifier =
            Modifier
                .height(150.dp)
                .width(100.dp)
                .background(color = FlintTheme.colors.gray300)
        )

        Spacer(modifier = Modifier.width(16.dp))

        CollectionCreateInfoSection(
            title = title,
            director = director,
            createdYear = createdYear,
            modifier = Modifier
        )
    }
}

@Composable
private fun CollectionCreateInfoSection(
    title: String,
    director: String,
    createdYear: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            color = FlintTheme.colors.white,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = FlintTheme.typography.head3M18
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = director,
            modifier = Modifier.fillMaxWidth(),
            color = FlintTheme.colors.gray300,
            style = FlintTheme.typography.body1R16
        )

        Text(
            text = createdYear,
            modifier = Modifier.fillMaxWidth(),
            color = FlintTheme.colors.gray300,
            style = FlintTheme.typography.body1R16
        )
    }
}

@Composable
private fun CollectionCreateFilmSectionImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier
    )
}

@Preview
@Composable
private fun CollectionCreateFilmSectionPreview() {
    FlintTheme {
        CollectionCreateFilmSection(
            imageUrl = "https://media.posterstore.com/site_images/68631db0…B0101-5.jpg?auto=compress%2Cformat&fit=max&w=3840",
            title = "해리포터 불의 잔",
            director = "메롱",
            createdYear = "2005"
        )
    }
}
