package com.flint.core.designsystem.component.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flint.R
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
    paddingValues: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    textStyle: TextStyle = FlintTheme.typography.body1R16,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingContent: @Composable () -> Unit = {}
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
            cursorBrush = SolidColor(FlintTheme.colors.gray300),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = if (maxLines == 1) Alignment.CenterStart else Alignment.TopStart
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
            onValueChange = { text = it }
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun FlintTextFieldCounterPreview() {
    FlintTheme {
        var text by remember { mutableStateOf("") }

        FlintBasicTextField(
            modifier = Modifier.fillMaxWidth(),
            placeholder = "닉네임",
            value = text,
            onValueChange = { text = it },
            trailingContent = {
                Text(
                    text = "${text.length}/20",
                    style = FlintTheme.typography.body2R14,
                    color = FlintTheme.colors.gray300
                )
            }
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun FlintTextFieldMultiLineWithLengthPreview() {
    FlintTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            var text by remember { mutableStateOf("") }

            FlintBasicTextField(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "컬렉션의 주제를 작성해주세요.",
                value = text,
                onValueChange = { text = it },
                height = 120.dp,
                maxLines = 5
            )

            Text(
                text = "${text.length}/200",
                style = FlintTheme.typography.caption1M12,
                color = FlintTheme.colors.white
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun FlintSearchTextFieldPreview() {
    FlintTheme {
        var text by remember { mutableStateOf("") }

        FlintBasicTextField(
            modifier = Modifier.fillMaxWidth(),
            placeholder = "작품 이름",
            value = text,
            onValueChange = { text = it },
            radius = 36.dp,
            height = 44.dp,
            maxLines = 1,
            paddingValues = PaddingValues(start = 16.dp),
            trailingContent = {
                Image(
                    modifier = Modifier
                        .padding(12.dp)
                        .size(24.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                    contentDescription = null
                )
            }
        )
    }
}