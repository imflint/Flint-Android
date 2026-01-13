package com.flint.core.designsystem.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
 * Displays a circular profile image with a bottom-right edit (camera) icon overlay.
 *
 * Tapping the icon invokes the provided edit callback.
 *
 * @param imageUrl URL of the profile image to display; may be empty to show a placeholder.
 * @param onClickEdit Lambda invoked when the edit icon is pressed.
 * @param modifier Optional [Modifier] applied to the outer container.
 */
@Composable
fun EditProfileImage(
    imageUrl: String,
    onClickEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(128.dp)
    ) {
        NetworkImage(
            imageUrl = imageUrl,
            shape = CircleShape,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .align(Alignment.Center)
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_profile_change),
            contentDescription = "camera",
            tint = Color.Unspecified,
            modifier =
            Modifier
                .align(Alignment.BottomEnd)
                .size(48.dp)
                .clickable { onClickEdit() }
        )
    }
}

/**
 * Displays an IDE preview of the EditProfileImage composable wrapped in FlintTheme.
 *
 * Renders the component inside a Box using the theme background, with an empty `imageUrl`
 * and a no-op `onClickEdit` handler for preview purposes.
 */
@Preview(showBackground = true)
@Composable
fun EditProfileImagePreview() {
    FlintTheme {
        Box(
            modifier = Modifier
                .background(FlintTheme.colors.background)
        ) {
            EditProfileImage(
                imageUrl = "",
                onClickEdit = {}
            )
        }
    }
}