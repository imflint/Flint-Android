package com.flint.core.designsystem.component.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chattymin.pebble.graphemeLength
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionInputTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    maxLength: Int,
    placeholder: String,
    singleLine: Boolean,
    maxLines: Int,
    isShowLengthTitle: Boolean,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.End,
    ) {
        FlintBasicTextField(
            placeholder = placeholder,
            value = value,
            onValueChange = onValueChanged,
            maxLength = maxLength,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            modifier = modifier,
        )

        if (!isShowLengthTitle) return

        Text(
            text = "${value.graphemeLength}/$maxLength",
            style = FlintTheme.typography.caption1M12,
            color = FlintTheme.colors.white,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xff000000)
@Composable
private fun CollectionInputTextFieldPreview() {
    FlintTheme {
        var text by remember { mutableStateOf("") }
        var text2 by remember { mutableStateOf("") }
        var text3 by remember { mutableStateOf("") }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            item {
                CollectionInputTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                    ,
                    value = text,
                    placeholder = "컬렉션의 주제를 작성해주세요.",
                    onValueChanged = { text = it },
                    maxLength = 20,
                    singleLine = true,
                    maxLines = 1,
                    isShowLengthTitle = true
                )
            }

            item {
                CollectionInputTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 104.dp),
                    value = text2,
                    placeholder = "컬렉션의 주제를 작성해주세요.",
                    onValueChanged = { text2 = it },
                    maxLength = 200,
                    singleLine = false,
                    maxLines = Int.MAX_VALUE,
                    isShowLengthTitle = true
                )
            }
            
            item {
                CollectionInputTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    value = text3,
                    placeholder = "컬렉션의 주제를 작성해주세요.",
                    onValueChanged = { text3 = it },
                    singleLine = false,
                    maxLength = Int.MAX_VALUE,
                    maxLines = Int.MAX_VALUE,
                    isShowLengthTitle = false
                )
            }
        }
    }
}
