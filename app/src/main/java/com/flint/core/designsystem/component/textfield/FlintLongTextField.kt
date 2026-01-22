package com.flint.core.designsystem.component.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chattymin.pebble.graphemeLength
import com.flint.core.designsystem.theme.FlintTheme
import kotlin.math.max
import kotlin.math.sin

@Composable
fun FlintLongTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    maxLength: Int,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    height: Dp = 120.dp
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.End,
    ) {
        FlintBasicTextField(
            modifier = modifier,
            placeholder = placeholder,
            value = value,
            onValueChange = onValueChanged,
            maxLength = maxLength,
            singleLine = singleLine,
            maxLines = 5,
            height = height,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
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
            modifier = Modifier.fillMaxWidth(),
            value = text,
            placeholder = "컬렉션의 주제를 작성해주세요.",
            onValueChanged = { text = it },
            maxLength = 200,
        )
    }
}
