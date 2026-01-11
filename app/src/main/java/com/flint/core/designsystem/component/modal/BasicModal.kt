package com.flint.core.designsystem.component.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.core.designsystem.theme.FlintTypography

@Composable
fun BasicModal(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties =
        DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = properties,
    ) {
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            brush = FlintTheme.colors.gradient700,
                            shape = RoundedCornerShape(12.dp),
                        ).shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = Color.Black.copy(alpha = 0.25f),
                            spotColor = Color.Black.copy(alpha = 0.25f),
                        ).padding(
                            top = 36.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 20.dp,
                        ),
            ) {
                content()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BasicModalPreview() {
    BasicModal(
        onDismiss = {},
        modifier = Modifier.background(Color.White),
    ) {
        Text(
            text = "기본 모달 컨텐츠",
            style = FlintTypography.body1M16,
            color = FlintTheme.colors.white,
        )
    }
}
