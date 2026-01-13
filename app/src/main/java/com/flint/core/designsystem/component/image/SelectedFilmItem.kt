package com.flint.core.designsystem.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
fun SelectedFilmItem(
    imageUrl: String,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(92.dp),
    ) {
        NetworkImage(
            imageUrl = imageUrl,
            shape = CircleShape,
            modifier =
                Modifier
                    .size(80.dp)
                    .align(Alignment.BottomStart),
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_deselect),
            contentDescription = "x",
            tint = Color.Unspecified,
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .size(48.dp)
                    .clickable { onRemoveClick() },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectedFilmItemPreview() {
    FlintTheme {
        Box(
            modifier =
                Modifier
                    .background(FlintTheme.colors.background),
        ) {
            SelectedFilmItem(
                imageUrl = "",
                onRemoveClick = {},
            )
        }
    }
}
