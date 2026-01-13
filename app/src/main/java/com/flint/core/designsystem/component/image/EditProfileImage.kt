package com.flint.core.designsystem.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun EditProfileImage(
    imageUrl: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(128.dp)
                .clickable { onEditClick() },
    ) {
        ProfileImage(
            imageUrl = imageUrl,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .align(Alignment.Center),
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_profile_change),
            contentDescription = "camera",
            tint = Color.Unspecified,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(48.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileImagePreview() {
    FlintTheme {
        Box(
            modifier =
                Modifier
                    .background(FlintTheme.colors.background),
        ) {
            EditProfileImage(
                imageUrl = "",
                onEditClick = {},
            )
        }
    }
}
