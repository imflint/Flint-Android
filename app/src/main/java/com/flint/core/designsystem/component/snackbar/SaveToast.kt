package com.flint.core.designsystem.component.snackbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun SaveToast(
    navigateToSavedCollection: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .padding(horizontal = 11.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(FlintTheme.colors.gray900)
                .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_gradient_bookmark),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
        )

        Spacer(Modifier.width(20.dp))

        Column {
            Text(
                text = "취향이 하나 더 쌓였어요",
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.body1Sb16,
            )

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.clickable(onClick = navigateToSavedCollection),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "저장한 컬렉션 보러가기",
                    color = FlintTheme.colors.gray200,
                    style = FlintTheme.typography.body2R14,
                )

                Icon(
                    painter = painterResource(R.drawable.ic_more),
                    contentDescription = null,
                    tint = FlintTheme.colors.gray200,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SaveToastPreview() {
    FlintTheme {
        SaveToast(
            navigateToSavedCollection = {},
        )
    }
}
