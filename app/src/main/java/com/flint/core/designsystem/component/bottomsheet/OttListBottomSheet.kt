package com.flint.core.designsystem.component.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.listItem.OttShortCutListItem
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.type.OttType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OttListBottomSheet(
    ottList: List<OttType>,
    onDismiss: () -> Unit,
    onMoveClick: (OttType) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    FlintBasicBottomSheet(
        sheetState = sheetState,
        onDismiss = onDismiss,
    ) {
        LazyColumn(
            modifier = modifier.padding(top = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(ottList.size) {
                OttShortCutListItem(
                    ottType = ottList[it],
                    onMoveClick = {
                        onMoveClick(ottList[it])
                        onDismiss()
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewOttListBottomSheet() {
    FlintTheme {
        val ottList =
            listOf(
                OttType.Netflix,
                OttType.Wave,
                OttType.Tving,
                OttType.Disney,
                OttType.Coupang,
                OttType.Watcha,
            )

        val sheetState = rememberModalBottomSheetState()

        OttListBottomSheet(
            ottList = ottList,
            onDismiss = {},
            onMoveClick = {},
            modifier = Modifier,
            sheetState = sheetState,
        )
    }
}
