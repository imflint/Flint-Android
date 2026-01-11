package com.flint.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintLogoTopAppbar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = FlintTheme.colors.background
) {
    FlintBasicTopAppbar(
        modifier = modifier,
        backgroundColor = backgroundColor,
        navigationIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.img_textlogo),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        },
    )
}

@Preview
@Composable
private fun FlintLogoTopAppbarPreview() {
    FlintTheme {
        FlintLogoTopAppbar(
            backgroundColor = Color.Transparent
        )
    }
}
