package com.flint.presentation.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.component.image.ProfileImage
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun ProfileTopSection(
    userName: String,
    profileUrl: String,
    isFliner: Boolean,
    modifier: Modifier = Modifier,
) {
    Column {
        Box(
            modifier =
                modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .paint(
                        painter = painterResource(id = R.drawable.img_collection_bg2),
                        contentScale = ContentScale.FillBounds,
                    ),
        ) {
            val gradient =
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF424BBD).copy(1f), Color(0xFF121212).copy(alpha = 0.04f)),
                )

            Column(
                modifier =
                    Modifier
                        .background(FlintTheme.colors.background)
                        .align(Alignment.BottomCenter),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(34.dp)
                            .background(gradient),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = userName,
                        style = FlintTheme.typography.display2M28,
                        color = FlintTheme.colors.white,
                    )
                    if (isFliner) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_qualified),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }

            ProfileImage(
                imageUrl = profileUrl,
                modifier =
                    Modifier
                        .offset(x = 13.dp, y = (-42).dp)
                        .align(Alignment.BottomStart)
                        .size(128.dp),
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun ProfileTopSectionPreview() {
    FlintTheme {
        ProfileTopSection(
            userName = "안두콩",
            profileUrl = "",
            isFliner = false,
        )
    }
}
