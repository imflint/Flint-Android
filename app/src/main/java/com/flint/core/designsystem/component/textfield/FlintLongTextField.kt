package com.flint.core.designsystem.component.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
fun FlintLongTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    maxLength: Int,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
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

        Text(
            text = "${value.graphemeLength}/$maxLength",
            style = FlintTheme.typography.caption1M12,
            color = FlintTheme.colors.white,
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun FlintLongTextFieldPreview() {
    FlintTheme {
        var text by remember { mutableStateOf("") }

        FlintLongTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            value = text,
            placeholder = "컬렉션의 주제를 작성해주세요.",
            onValueChanged = { text = it },
            maxLength = 200,
        )
    }
}
