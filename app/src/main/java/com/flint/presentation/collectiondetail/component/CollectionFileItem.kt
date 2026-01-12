package com.flint.presentation.collectiondetail.component

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.dropShadow
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionFileItem() {
    Box(
        modifier = Modifier
            .size(width = 154.dp, height = 154.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(FlintTheme.colors.gray800)
    ) {
        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 120.dp)
                .offset(x = 16.dp, y = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        )

        Box(
            modifier = Modifier
                .dropShadow(
                    shape = CircleShape,
                    color = Color(0xFF000000).copy(alpha = 0.15f),
                    offsetX = (-4).dp,
                    offsetY = 0.dp,
                    blur = 10.dp,
                    spread = 0.dp
                )
                .size(width = 80.dp, height = 120.dp)
                .align(Alignment.TopCenter)
                .rotate(15f)
                .offset(x = 20.dp, y = 15.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(108.dp)
                .align(Alignment.BottomEnd)
                .paint(
                    painter = painterResource(id = R.drawable.img_folder_fg),
                    contentScale = ContentScale.FillWidth
                )
                .padding(12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .align(Alignment.TopEnd)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_bookmark_fill),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        "24",
                        style = TextStyle(
                            color = Color.White
                        ),
                        modifier = Modifier
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
            ){
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_avatar_blue),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                )
                Text(
                    "닉네임",
                    style = FlintTheme.typography.caption1M12,
                    color = Color.White
                )
            }

        }
    }
}

@Preview(showBackground = false)
@Composable
private fun CollectionFileItemPreview() {
    FlintTheme {
        CollectionFileItem()
    }
}