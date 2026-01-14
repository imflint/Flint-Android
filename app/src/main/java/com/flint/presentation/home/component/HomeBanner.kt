package com.flint.presentation.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun HomeBanner(
    userName: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(270.dp)
                .paint(
                    painter = painterResource(id = R.drawable.img_collection_bg2),
                    contentScale = ContentScale.FillBounds,
                ),
    ) {
        Text(
            text = "반가워요, $userName 님\n오늘은 어떤 작품이 끌리세요?",
            style = FlintTheme.typography.head1Sb22,
            color = FlintTheme.colors.white,
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 30.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewHomeBanner() {
    FlintTheme {
        HomeBanner(
            userName = "종우",
        )
    }
}
