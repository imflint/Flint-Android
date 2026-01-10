package com.flint.core.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintBasicTextField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    height: Dp = 40.dp,
    radius: Dp = 8.dp,
    trailingContent: @Composable () -> Unit = {},
    textStyle: TextStyle = FlintTheme.typography.body1R16,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(radius))
            .background(FlintTheme.colors.gray800)
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxSize(),
            value = value,
            textStyle = textStyle.copy(color = FlintTheme.colors.white),
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            maxLines = maxLines,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (value.isEmpty()) { // PlaceHolder
                            Text(
                                text = placeholder,
                                style = textStyle,
                                color = FlintTheme.colors.gray300
                            )
                        }
                        innerTextField()
                    }
                    trailingContent()
                }
            }
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun BasicTextFieldPreview() {
    FlintTheme {
        var text by remember { mutableStateOf("") }

        FlintBasicTextField(
            modifier = Modifier.fillMaxWidth(),
            placeholder = "PlaceHolder",
            value = text,
            onValueChange = { text = it },
            trailingContent = {},
        )
    }
}