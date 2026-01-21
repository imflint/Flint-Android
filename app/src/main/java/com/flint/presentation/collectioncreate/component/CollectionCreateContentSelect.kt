package com.flint.presentation.collectioncreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionCreateContentSelect(
    onCheckClick: () -> Unit,
    isSelected: Boolean,
    imageUrl: String,
    title: String,
    director: String,
    createdYear: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = FlintTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CollectionCreateContentSection(
            imageUrl = imageUrl,
            title = title,
            director = director,
            createdYear = createdYear,
            modifier = Modifier.weight(1f),
        )

        CollectionCreateContentSelectTag(
            isSelected = isSelected,
            onClick = onCheckClick,
        )
    }
}

@Composable
fun CollectionCreateContentSelectTag(
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Icon(
        imageVector = ImageVector.vectorResource(if (isSelected) R.drawable.ic_check_fill else R.drawable.ic_check_empty),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier =
            Modifier
                .size(48.dp)
                .clickable(onClick = onClick),
    )
}

@Preview
@Composable
private fun CollectionCreateContentSectionPreview() {
    FlintTheme {
        var isSelected by remember { mutableStateOf(false) }
        CollectionCreateContentSelect(
            onCheckClick = { isSelected = !isSelected },
            isSelected = isSelected,
            imageUrl = "https://buly.kr/DEaVFRZ",
            title = "해리포터 불의 잔",
            director = "메롱",
            createdYear = 2005,
        )
    }
}
