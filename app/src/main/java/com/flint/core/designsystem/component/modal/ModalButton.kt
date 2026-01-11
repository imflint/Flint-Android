package com.flint.core.designsystem.component.modal

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.core.designsystem.theme.FlintTypography

enum class ModalButtonType {
    CONFIRM, // 확인
    CANCEL, // 취소
    DESTRUCTIVE, // 삭제/위험
}

@Composable
fun ModalButton(
    text: String,
    onClick: () -> Unit,
    type: ModalButtonType,
    modifier: Modifier = Modifier,
) {
    val (backgroundColor, textColor) =
        when (type) {
            ModalButtonType.CONFIRM -> FlintTheme.colors.primary400 to FlintTheme.colors.white
            ModalButtonType.CANCEL -> FlintTheme.colors.gray100 to FlintTheme.colors.gray800
            ModalButtonType.DESTRUCTIVE -> FlintTheme.colors.error500 to FlintTheme.colors.white
        }
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(8.dp))
                .background(backgroundColor)
                .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = FlintTypography.body1Sb16,
            color = textColor,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ModalButtonPreview() {
    FlintTheme {
        Column(
            modifier =
                Modifier
                    .background(FlintTheme.colors.white)
                    .padding(20.dp),
        ) {
            ModalButton(
                text = "확인",
                onClick = {},
                type = ModalButtonType.CONFIRM,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            ModalButton(
                text = "취소",
                onClick = {},
                type = ModalButtonType.CANCEL,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            ModalButton(
                text = "삭제",
                onClick = {},
                type = ModalButtonType.DESTRUCTIVE,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
