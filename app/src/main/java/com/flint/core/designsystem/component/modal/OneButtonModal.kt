package com.flint.core.designsystem.component.modal

import androidx.annotation.DrawableRes
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
import com.flint.core.designsystem.component.button.ModalButton
import com.flint.core.designsystem.component.button.ModalButtonType
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun OneButtonModal(
    message: String,
    buttonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    @DrawableRes icon: Int,
) {
    BasicModal(
        onDismiss = onDismiss,
        modifier = modifier,
    ) {
        // 아이콘 영역
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
        )

        Spacer(modifier = Modifier.height(if (title != null) 4.dp else 12.dp))

        // 제목 (옵션)
        if (title != null) {
            Text(
                text = title,
                style = FlintTheme.typography.head1Sb22,
                color = FlintTheme.colors.white,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // 메시지
        Text(
            text = message,
            style = FlintTheme.typography.body1M16,
            color = FlintTheme.colors.white,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 버튼
        ModalButton(
            text = buttonText,
            onClick = onConfirm,
            type = ModalButtonType.CONFIRM,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OneButtonModalWithTitlePreview() {
    FlintTheme {
        OneButtonModal(
            modifier = Modifier.background(Color.White),
            title = "법최 스릴러 매니아",
            message = "새로운 뱃지를 획득했어요!",
            buttonText = "확인",
            onConfirm = {},
            onDismiss = {},
            icon = R.drawable.ic_gradient_check,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OneButtonModalNoTitlePreview() {
    FlintTheme {
        OneButtonModal(
            modifier = Modifier.background(Color.White),
            message = "새로운 뱃지를 획득했어요!",
            buttonText = "확인",
            onConfirm = {},
            onDismiss = {},
            icon = R.drawable.ic_gradient_check,
        )
    }
}
