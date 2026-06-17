package com.flint.presentation.collectiondetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionCopyrightFooter(
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Flint에서 제공하는 영화 · 드라마를 포함한 모든 콘텐츠의 저작권은 각 권리자에게 있으며, 관련 법령에 따라 보호됩니다.\n " +
                "컬렉션 이용 시 저작권을 준수해 주세요.",
        color = FlintTheme.colors.gray300,
        style = FlintTheme.typography.body2R14,
        modifier = modifier
            .fillMaxWidth()
            .background(color = FlintTheme.colors.gray800)
            .padding(horizontal = 16.dp, vertical = 32.dp),
    )
}

@Preview
@Composable
private fun CollectionCopyrightFooterPreview() {
    FlintTheme {
        CollectionCopyrightFooter()
    }
}
