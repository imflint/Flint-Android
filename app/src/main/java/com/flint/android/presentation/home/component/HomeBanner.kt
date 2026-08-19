package com.flint.android.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.android.R
import com.flint.android.core.designsystem.theme.FlintTheme

@Composable
fun HomeBanner(
    userName: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(230.dp)
                .paint(
                    painter = painterResource(id = R.drawable.img_home_banner),
                    contentScale = ContentScale.FillBounds,
                ),
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_textlogo),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 54.dp)
                .width(90.dp)
                .height(20.dp)
        )

        Text(
            text = "반가워요, $userName 님\n오늘은 어떤 작품이 끌리세요?",
            style = FlintTheme.typography.head1Sb22,
            color = FlintTheme.colors.white,
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 20.dp),
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
