package com.flint.core.designsystem.component.toggle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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

/**
 * A Flint-themed toggle switch that displays the current state and delegates state updates to the caller.
 *
 * The switch uses Flint design system colors and a 24.dp thumb. Tapping the toggle invokes `onCheckedChange`
 * with the new state (the inverse of the current `isChecked`).
 *
 * @param isChecked Current checked state of the toggle.
 * @param onCheckedChange Callback invoked with the new checked state when the user toggles the switch.
 * @param modifier Optional [Modifier] applied to the switch (e.g., for layout or additional click handling).
 */
@Composable
fun FlintBasicToggle(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Switch(
        checked = isChecked,
        onCheckedChange = null,
        modifier =
            modifier
                .noRippleClickable { onCheckedChange(!isChecked) }
                .padding(vertical = 10.dp),
        thumbContent = {
            Box(
                modifier = Modifier.size(24.dp),
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