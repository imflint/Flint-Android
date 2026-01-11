package com.flint.core.designsystem.component.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.flint.R
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.core.designsystem.theme.FlintTypography

@Composable
fun OneButtonModal(
    message: String,
    buttonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: @Composable () -> Unit,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = false
    )
) {
    BasicModal(
        onDismiss = onDismiss,
        modifier = modifier,
        properties = properties
    ) {
        // 아이콘 영역
        icon()

        Spacer(modifier = Modifier.height(if (title != null) 4.dp else 12.dp))

        // 제목 (옵션)
        if (title != null) {
            Text(
                text = title,
                style = FlintTypography.head1Sb22,
                color = FlintTheme.colors.white,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // 메시지
        Text(
            text = message,
            style = FlintTypography.body1M16,
            color = FlintTheme.colors.white,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 버튼
        ModalButton(
            text = buttonText,
            onClick = onConfirm,
            backgroundColor = FlintTheme.colors.primary400,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OneButtonModalWithTitlePreview() {
    OneButtonModal(
        modifier = Modifier.background(Color.White),
        title = "법최 스릴러 매니아",
        message = "새로운 뱃지를 획득했어요!",
        buttonText = "확인",
        onConfirm = {},
        onDismiss = {},
        icon = {
            Image(
                painter = painterResource(id = R.drawable.ic_gradient_check),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun OneButtonModalNoTitlePreview() {
    OneButtonModal(
        modifier = Modifier.background(Color.White),
        message = "새로운 뱃지를 획득했어요!",
        buttonText = "확인",
        onConfirm = {},
        onDismiss = {},
        icon = {
            Image(
                painter = painterResource(id = R.drawable.ic_gradient_check),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }
    )
}