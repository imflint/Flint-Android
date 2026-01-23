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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.component.image.ProfileImage
import com.flint.core.designsystem.theme.FlintTheme

private const val EASTER_EGG_CLICK_COUNT = 5
private const val EASTER_EGG_CLICK_TIMEOUT = 3000L

@Composable
fun ProfileTopSection(
    userName: String,
    profileUrl: String,
    isFliner: Boolean,
    modifier: Modifier = Modifier,
    onEasterEggWithdraw: () -> Unit = {},
) {
    var clickCount by remember { mutableIntStateOf(0) }
    var lastClickTime by remember { mutableLongStateOf(0L) }

    Column {
        Box(
            modifier =
                modifier
                    .fillMaxWidth()
                    .height(306.dp)
                    .paint(
                        painter = painterResource(id = R.drawable.img_collection_bg2),
                        contentScale = ContentScale.FillBounds,
                    ),
        ) {
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
                            .background(FlintTheme.colors.myGradient),
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
                        .size(128.dp)
                        .noRippleClickable(
                            onClick = {
                                val currentTime = System.currentTimeMillis()
                                if (currentTime - lastClickTime > EASTER_EGG_CLICK_TIMEOUT) {
                                    clickCount = 1
                                } else {
                                    clickCount++
                                }
                                lastClickTime = currentTime

                                if (clickCount >= EASTER_EGG_CLICK_COUNT) {
                                    clickCount = 0
                                    onEasterEggWithdraw()
                                }
                            }
                        )
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
