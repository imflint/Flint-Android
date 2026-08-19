package com.flint.android.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.android.core.designsystem.theme.FlintTheme

@Composable
fun InfoModalTrigger(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = FlintTheme.colors.gray800,
                shape = RoundedCornerShape(size = 12.dp),
            )
            .padding(horizontal = 12.dp, vertical = 14.dp),
    ) {
        Text(
            text = text,
            style = FlintTheme.typography.body2R14,
            color = FlintTheme.colors.gray300,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF141417)
@Composable
private fun InfoModalTriggerPreview() {
    FlintTheme {
        InfoModalTrigger(text = "저장한 작품들에서 반복되는 키워드를 분석해 취향 키워드를 만들어요. n개 이상 작품이 쌓이면 업데이트할 수 있어요.")
    }
}
