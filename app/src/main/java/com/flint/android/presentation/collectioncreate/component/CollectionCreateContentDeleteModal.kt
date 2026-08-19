package com.flint.android.presentation.collectioncreate.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flint.android.R
import com.flint.android.core.designsystem.component.modal.TwoButtonModal
import com.flint.android.core.designsystem.theme.FlintTheme

@Composable
fun CollectionCreateContentDeleteModal(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    TwoButtonModal(
        title = "작품을 삭제할까요?",
        message = "작성한 내용이 모두 삭제돼요.",
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
private fun CollectionCreateContentDeleteModalPreview() {
    FlintTheme {
        CollectionCreateContentDeleteModal(
            onConfirm = {},
            onDismiss = {},
        )
    }
}
