package com.flint.core.designsystem.component.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintSearchTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onSearchAction: () -> Unit = {},
    onClearAction: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    FlintBasicTextField(
        modifier = modifier.fillMaxWidth(),
        placeholder = placeholder,
        value = value,
        onValueChange = onValueChanged,
        radius = 36.dp,
        height = 44.dp,
        maxLines = 1,
        paddingValues = PaddingValues(start = 16.dp),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingContent = {
            Image(
                modifier =
                    Modifier
                        .padding(12.dp)
                        .size(24.dp)
                        .noRippleClickable(
                            onClick = {
                                keyboardController?.hide()
                                if (value.isNotEmpty()) {
                                    onValueChanged("")
                                    onClearAction()
                                }
                            },
                        ),
                imageVector = ImageVector.vectorResource(
                    id = if (value.isNotEmpty()) R.drawable.ic_cancel else R.drawable.ic_search
                ),
                contentDescription = null,
            )
        },
    )
}

@Preview(showBackground = false)
@Composable
private fun FlintSearchTextFieldPreview() {
    FlintTheme {
        var text by remember { mutableStateOf("") }

        FlintSearchTextField(
            placeholder = "작품 이름",
            value = text,
            onValueChanged = { text = it },
        )
    }
}
