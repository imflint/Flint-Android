package com.flint.core.designsystem.component.toggle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintBasicToggle(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Switch(
        checked = isChecked,
        onCheckedChange = null,
        modifier = modifier.noRippleClickable { onCheckedChange(!isChecked) },
        thumbContent = {
            Box(
                modifier = Modifier.size(28.dp),
            )
        },
        colors =
            SwitchDefaults.colors(
                checkedThumbColor = FlintTheme.colors.white,
                checkedTrackColor = FlintTheme.colors.secondary400,
                uncheckedThumbColor = FlintTheme.colors.white,
                uncheckedTrackColor = FlintTheme.colors.gray200,
                checkedBorderColor = Color.Transparent,
                uncheckedBorderColor = Color.Transparent,
            ),
    )
}

@Preview
@Composable
private fun FlintBasicTogglePreview() {
    FlintTheme {
        var checked by remember { mutableStateOf(false) }

        FlintBasicToggle(
            isChecked = checked,
            onCheckedChange = { checked = it },
        )
    }
}
