package com.flint.core.designsystem.component.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.theme.FlintColors
import com.flint.core.designsystem.theme.FlintTypography

@Composable
fun ModalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = FlintColors.primary400,
    textColor: Color = FlintColors.white
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = FlintTypography.body1Sb16,
            color = textColor
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ModalButtonPreview() {
    Column(
        modifier = Modifier
            .background(FlintColors.white)
            .padding(20.dp)
    ) {
        ModalButton(
            text = "확인",
            onClick = {},
            backgroundColor = FlintColors.primary400,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        ModalButton(
            text = "취소",
            onClick = {},
            backgroundColor = FlintColors.gray100,
            textColor = FlintColors.background,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        ModalButton(
            text = "삭제",
            onClick = {},
            backgroundColor = FlintColors.error500,
            modifier = Modifier.fillMaxWidth()
        )
    }
}