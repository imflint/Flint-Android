package com.flint.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.interaction.pressClickable
import com.flint.core.designsystem.interaction.pressScale
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintSaveNoneButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier =
            modifier
                .padding(vertical = 4.dp)
                .heightIn(min = 40.dp)
                .pressScale(interactionSource)
                .clip(RoundedCornerShape(44.dp))
                .background(FlintTheme.colors.gradient700)
                .border(1.dp, FlintTheme.colors.buttonStroke, RoundedCornerShape(44.dp))
                .pressClickable(
                    interactionSource = interactionSource,
                    role = Role.Button,
                    onClick = onClick,
                )
                .padding(horizontal = 16.dp, vertical = 9.dp),
    ) {
        Text(
            "컬렉션 저장 +",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.body1Sb16,
        )
    }
}

@Preview
@Composable
private fun FlintSaveNoneButtonPreview() {
    FlintTheme {
        FlintSaveNoneButton({})
    }
}
