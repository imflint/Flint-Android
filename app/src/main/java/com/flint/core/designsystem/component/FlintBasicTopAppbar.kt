package com.flint.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
fun FlintBasicTopAppbar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = FlintTheme.colors.background,
    title: String = "",
    navigationIcon: @Composable () -> Unit,
    action: @Composable () -> Unit = {}
) {
    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationIcon()

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.body1M16
            )
        }

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            action()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlintBasicTopAppbarPreview() {
    FlintTheme {
        FlintBasicTopAppbar(
//            backgroundColor = Color.Transparent,
            title = "전체 컬렉션",
            navigationIcon = {
                Icon(
                    modifier = Modifier,
                    imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                    contentDescription = null,
                    tint = FlintTheme.colors.white
                )
            },
            action = {
                Icon(
                    modifier = Modifier,
                    imageVector = ImageVector.vectorResource(R.drawable.ic_cancel),
                    contentDescription = null,
                    tint = FlintTheme.colors.white
                )
            }
        )
    }
}
