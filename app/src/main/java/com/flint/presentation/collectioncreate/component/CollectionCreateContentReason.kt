package com.flint.presentation.collectioncreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.component.textfield.CollectionInputTextField
import com.flint.core.designsystem.component.toggle.FlintBasicToggle
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionCreateContentReason(
    selectedReason: String,
    onSelectedReasonChanged: (String) -> Unit,
    onSelectImageClick: () -> Unit,
    isSpoiler: Boolean,
    onSpoilerChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    Column(modifier = modifier) {
        Text(
            text = "이 작품을 선택한 이유",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.head3M18,
        )

        Spacer(Modifier.height(16.dp))

        CollectionInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 104.dp),
            value = selectedReason,
            placeholder = "이 작품의 매력 포인트를 적어주세요.",
            onValueChanged = onSelectedReasonChanged,
            singleLine = false,
            maxLength = Int.MAX_VALUE,
            maxLines = Int.MAX_VALUE,
            isShowLengthTitle = false,
            isError = isError,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 48.dp, height = 28.dp)
                    .border(1.dp, Color(0xFF1ABFF2), RoundedCornerShape(60.dp))
                    .clip(RoundedCornerShape(60.dp))
                    .background(Color(0xFF21242C))
                    .clickable(onClick = onSelectImageClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_background_photo),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(14.dp),
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "스포일러",
                    color = FlintTheme.colors.white,
                    style = FlintTheme.typography.caption1M12,
                )

                Spacer(Modifier.width(8.dp))

                FlintBasicToggle(
                    isChecked = isSpoiler,
                    onCheckedChange = onSpoilerChanged,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun CollectionCreateContentReasonPreview() {
    FlintTheme {
        var reason by remember { mutableStateOf("") }
        var isSpoiler by remember { mutableStateOf(false) }
        CollectionCreateContentReason(
            selectedReason = reason,
            onSelectedReasonChanged = { reason = it },
            onSelectImageClick = {},
            isSpoiler = isSpoiler,
            onSpoilerChanged = { isSpoiler = it },
        )
    }
}

