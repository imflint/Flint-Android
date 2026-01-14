package com.flint.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.button.FlintBasicButton
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun HomeRecentCollectionEmpty(
    navigateToExplore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "아직 읽어본 컬렉션이 없어요",
            style = FlintTheme.typography.head3Sb18,
            color = FlintTheme.colors.white,
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "천천히 둘러보며 끌리는 취향을 발견해보세요",
            style = FlintTheme.typography.body2R14,
            color = FlintTheme.colors.gray100,
        )

        Spacer(Modifier.height(24.dp))

        FlintBasicButton(
            text = "취향 발견하러 가기",
            state = FlintButtonState.Able,
            onClick = navigateToExplore,
            contentPadding = PaddingValues(),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(48.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewHomeRecentCollectionEmpty() {
    FlintTheme {
        HomeRecentCollectionEmpty(
            navigateToExplore = {},
        )
    }
}
