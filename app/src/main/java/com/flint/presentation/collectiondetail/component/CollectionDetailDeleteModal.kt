package com.flint.presentation.collectiondetail.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flint.R
import com.flint.core.designsystem.component.modal.TwoButtonModal
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionDetailDeleteModal(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    TwoButtonModal(
        title = "컬렉션을 삭제할까요?",
        message = "내가 작성한 컬렉션이 삭제돼요.",
        cancelText = "취소",
        confirmText = "삭제",
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        icon = R.drawable.ic_gradient_trash,
        isDestructive = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun CollectionDetailDeleteModalPreview() {
    FlintTheme {
        CollectionDetailDeleteModal(
            onConfirm = {},
            onDismiss = {},
        )
    }
}
