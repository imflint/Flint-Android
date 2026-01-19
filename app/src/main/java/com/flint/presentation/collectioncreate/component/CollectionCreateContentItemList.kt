package com.flint.presentation.collectioncreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.component.textfield.FlintBasicTextField
import com.flint.core.designsystem.component.toggle.FlintBasicToggle
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionCreateContentItemList(
    onCancelClick: () -> Unit,
    imageUrl: String,
    title: String,
    director: String,
    createdYear: String,
) {
    var toggleChecked by remember { mutableStateOf(false) }
    var reasonText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.background(color = FlintTheme.colors.background),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_cancel),
                contentDescription = null,
                tint = Color.White,
                modifier =
                    Modifier
                        .clickable(onClick = onCancelClick)
                        .padding(12.dp)
                        .size(24.dp),
            )
        }

        Spacer(Modifier.height(16.dp))

        CollectionCreateContentSection(
            imageUrl = imageUrl,
            title = title,
            director = director,
            createdYear = createdYear,
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "이 작품을 선택한 이유",
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.head3M18,
            )

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
                    isChecked = toggleChecked,
                    onCheckedChange = { toggleChecked = it },
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        FlintBasicTextField(
            placeholder = "이 작품의 매력 포인트를 적어주세요.",
            value = reasonText,
            onValueChange = { reasonText = it },
            modifier = Modifier.fillMaxWidth(),
            height = 108.dp,
            maxLines = Int.MAX_VALUE,
            textStyle = FlintTheme.typography.body1R16,
            paddingValues =
                PaddingValues(
                    horizontal = 12.dp,
                    vertical = 10.dp,
                ),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun CollectionCreateContentItemListPreview() {
    FlintTheme {
        CollectionCreateContentItemList(
            onCancelClick = {},
            imageUrl = "https://buly.kr/DEaVFRZ",
            title = "해리포터 불의 잔 해리포터 불의 잔 해리포터 불의 잔 해리포터 불의 잔 해리포터 불의 잔 해리포터 불의 잔",
            director = "메롱",
            createdYear = "2005",
        )
    }
}
