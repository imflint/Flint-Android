package com.flint.core.designsystem.component.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintToast(
    text: String,
    imageVector: ImageVector?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .clip(RoundedCornerShape(44.dp))
                .background(FlintTheme.colors.gray700)
                .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (imageVector != null) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = Color.Unspecified,
            )

            Spacer(Modifier.width(8.dp))
        }

        Text(
            text = text,
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.body2R14,
        )
    }
}

@Preview
@Composable
private fun FlintToastPreview(
    @PreviewParameter(FlintToastPreviewParameterProvider::class) drawableRes: Int?,
) {
    FlintTheme {
        val imageVector = if (drawableRes == null) null else ImageVector.vectorResource(drawableRes)

        FlintToast(
            text = "텍스트를 입력해주세요",
            imageVector = imageVector,
        )
    }
}

class FlintToastPreviewParameterProvider : PreviewParameterProvider<Int?> {
    override val values: Sequence<Int?> =
        sequenceOf(
            R.drawable.ic_check,
            R.drawable.ic_x,
            null,
        )
}
