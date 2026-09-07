package com.flint.android.presentation.collectioncreate.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flint.android.R
import com.flint.android.core.designsystem.component.modal.TwoButtonModal
import com.flint.android.core.designsystem.theme.FlintTheme

@Composable
fun CollectionEditLeaveModal(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    TwoButtonModal(
        title = "컬렉션 수정을 그만둘까요?",
        message = "변경한 내용은 저장되지 않아요.",
        cancelText = "취소",
        confirmText = "나가기",
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        icon = R.drawable.ic_gradient_trash,
        isDestructive = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun CollectionEditLeaveModalPreview() {
    FlintTheme {
        CollectionEditLeaveModal(
            onConfirm = {},
            onDismiss = {},
        )
    }
}
