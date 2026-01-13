package com.flint.presentation.collectioncreate.component

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.flint.R
import com.flint.core.designsystem.component.modal.TwoButtonModal
import com.flint.core.designsystem.theme.FlintTheme

@Composable
private fun CollectionCreateModal() {
    TwoButtonModal(
        modifier = Modifier.background(Color.White),
        message = "작성한 내용이 모두 삭제돼요.",
        cancelText = "취소",
        confirmText = "삭제",
        onConfirm = {},
        onDismiss = {},
        title = "작품을 삭제할까요?",
        icon = R.drawable.ic_gradient_trash,
        isDestructive = true
    )
}

@Preview(showBackground = true)
@Composable
private fun CollectionCreateModalPreview() {
    FlintTheme {
        CollectionCreateModal()
    }
}
