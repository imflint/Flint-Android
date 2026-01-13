package com.flint.core.designsystem.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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

/**
 * Displays a circular film thumbnail with a dismiss icon anchored at the top-right.
 *
 * The thumbnail loads the image from [imageUrl]; tapping the dismiss icon calls [onClickRemove].
 *
 * @param imageUrl URL or resource identifier for the image to display inside the circular frame.
 * @param onClickRemove Callback invoked when the remove/deselect icon is clicked.
 * @param modifier Modifier applied to the component's container Box.
 */
@Composable
fun SelectedFilmItem(
    imageUrl: String,
    onClickRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(80.dp)
    ) {
        NetworkImage(
            imageUrl = imageUrl,
            shape = CircleShape,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_deselect),
            contentDescription = "x",
            tint = Color.Unspecified,
            modifier =
            Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .clickable { onClickRemove() }
        )
    }
}

/**
 * Preview composable that displays SelectedFilmItem inside the FlintTheme.
 *
 * Renders the item with an empty image URL and a no-op removal callback for design-time preview.
 */
@Preview(showBackground = true)
@Composable
fun SelectedFilmItemPreview() {
    FlintTheme {
        Box(
            modifier = Modifier
                .background(FlintTheme.colors.background)
        ) {
            SelectedFilmItem(
                imageUrl = "",
                onClickRemove = {}
            )
        }
    }
}