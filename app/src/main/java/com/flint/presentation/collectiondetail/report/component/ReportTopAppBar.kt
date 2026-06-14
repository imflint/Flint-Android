package com.flint.presentation.collectiondetail.report.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun ReportTopAppBar(
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(
            text = "신고",
            modifier = Modifier.align(Alignment.Center),
            style = FlintTheme.typography.body1M16,
            color = FlintTheme.colors.white,
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .noRippleClickable(onClick = onCancelClick)
                .size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_cancel),
                contentDescription = null,
                tint = FlintTheme.colors.white,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ReportTopAppBarPreview() {
    FlintTheme {
        ReportTopAppBar(
            onCancelClick = {},
        )
    }
}
