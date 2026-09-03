package com.flint.android.presentation.collectioncreate.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flint.android.R
import com.flint.android.core.designsystem.component.modal.TwoButtonModal
import com.flint.android.core.designsystem.theme.FlintTheme

@Composable
fun CollectionCreateLeaveModal(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    TwoButtonModal(
        title = "컬렉션 작성을 그만둘까요?",
        message = "작성한 내용이 모두 삭제돼요.",
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
private fun CollectionCreateLeaveModalPreview() {
    FlintTheme {
        CollectionCreateLeaveModal(
            onConfirm = {},
            onDismiss = {},
        )
    }
}
