package com.flint.presentation.collectiondetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.theme.FlintTheme

private val DropdownMenuItemShape = RoundedCornerShape(8.dp)
private val DropdownMenuItemPadding = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)

@Composable
fun CollectionReportDropdownMenuItem(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(color = FlintTheme.colors.gray700, shape = DropdownMenuItemShape)
            .noRippleClickable(onClick = onClick)
            .then(DropdownMenuItemPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "신고",
            color = FlintTheme.colors.gray100,
            style = FlintTheme.typography.body1M16,
        )
    }
}

@Composable
fun CollectionEditDeleteDropdownMenuItem(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Column(
        modifier = Modifier.background(color = FlintTheme.colors.gray700, shape = DropdownMenuItemShape),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable(onClick = onEditClick)
                .then(DropdownMenuItemPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "수정",
                color = FlintTheme.colors.gray100,
                style = FlintTheme.typography.body1M16,
            )
        }

        HorizontalDivider(color = FlintTheme.colors.gray400, thickness = 1.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable(onClick = onDeleteClick)
                .then(DropdownMenuItemPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "삭제",
                color = FlintTheme.colors.error500,
                style = FlintTheme.typography.body1M16,
            )
        }
    }
}
