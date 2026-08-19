package com.flint.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.interaction.pressClickable
import com.flint.core.designsystem.interaction.pressScale
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun HomeFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier =
            modifier
                .pressScale(interactionSource)
                .clip(CircleShape)
                .background(FlintTheme.colors.gradient400Secondary)
                .size(56.dp)
                .pressClickable(
                    interactionSource = interactionSource,
                    role = Role.Button,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
            tint = Color.Unspecified,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
        )
    }
}

@Preview
@Composable
private fun PreviewHomeFab() {
    FlintTheme {
        HomeFab(
            onClick = {},
        )
    }
}
