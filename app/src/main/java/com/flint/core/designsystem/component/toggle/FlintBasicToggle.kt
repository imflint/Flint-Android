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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.interaction.FlintPressDefaults
import com.flint.core.designsystem.interaction.flintClickable
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
        modifier =
            modifier
                .flintClickable(
                    // 트랙이 캡슐 모양이라 사각 딤이 튀어나온다. 축소만 쓴다.
                    pressedScale = FlintPressDefaults.ICON_SCALE,
                    // 로컬 상태 토글이라 연속으로 껐다 켜는 조작을 막으면 안 된다.
                    throttleMillis = 0L,
                    role = Role.Switch,
                    indication = null,
                    onClick = { onCheckedChange(!isChecked) },
                )
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
