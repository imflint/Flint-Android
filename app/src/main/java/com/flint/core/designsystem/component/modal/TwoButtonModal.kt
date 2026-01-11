package com.flint.core.designsystem.component.modal

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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

@Composable
fun TwoButtonModal(
    message: String,
    cancelText: String,
    confirmText: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    @DrawableRes icon: Int,
    isDestructive: Boolean = false, // true = 삭제 (빨간색 버튼)
    properties: DialogProperties =
        DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
) {
    BasicModal(
        onDismiss = onDismiss,
        modifier = modifier,
        properties = properties,
    ) {
        // 아이콘 영역
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
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

        // 버튼 2개
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            ModalButton(
                text = cancelText,
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                type = ModalButtonType.CANCEL,
            )
            ModalButton(
                text = confirmText,
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                type = if (isDestructive) ModalButtonType.DESTRUCTIVE else ModalButtonType.CONFIRM,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TwoButtonModalWithTitlePreview() {
    FlintTheme {
        TwoButtonModal(
            modifier = Modifier.background(Color.White),
            title = "법최 스릴러 매니아",
            message = "새로운 뱃지를 획득했어요!",
            cancelText = "취소",
            confirmText = "확인",
            onCancel = {},
            onConfirm = {},
            onDismiss = {},
            isDestructive = false,
            icon = R.drawable.ic_gradient_check,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TwoButtonModalDestructivePreview() {
    FlintTheme {
        TwoButtonModal(
            modifier = Modifier.background(Color.White),
            message = "새로운 뱃지를 획득했어요!",
            cancelText = "취소",
            confirmText = "삭제",
            onCancel = {},
            onConfirm = {},
            onDismiss = {},
            isDestructive = true,
            icon = R.drawable.ic_gradient_trash,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TwoButtonModalNoTitlePreview() {
    FlintTheme {
        TwoButtonModal(
            modifier = Modifier.background(Color.White),
            message = "새로운 뱃지를 획득했어요!",
            cancelText = "취소",
            confirmText = "확인",
            onCancel = {},
            onConfirm = {},
            onDismiss = {},
            icon = R.drawable.ic_gradient_check,
        )
    }
}
