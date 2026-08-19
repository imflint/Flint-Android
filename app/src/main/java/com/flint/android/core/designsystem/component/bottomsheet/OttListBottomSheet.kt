package com.flint.android.core.designsystem.component.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.android.core.designsystem.component.listItem.OttShortCutListItem
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.model.ott.OttListModel
import com.flint.android.domain.type.OttType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OttListBottomSheet(
    ottList: OttListModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    FlintBasicBottomSheet(
        sheetState = sheetState,
        onDismiss = onDismiss,
    ) {
        LazyColumn(
            modifier = modifier.padding(bottom = 30.dp),
        ) {
            item {
                Text(
                    text = "이 작품을 볼 수 있는 OTT",
                    style = FlintTheme.typography.head3Sb18,
                    color = FlintTheme.colors.white,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))
            }

            items(ottList.otts.size) {
                OttShortCutListItem(
                    ottModel = ottList.otts[it],
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewOttListBottomSheet() {
    FlintTheme {
        val ottList = OttListModel()

        val sheetState = rememberModalBottomSheetState()

        OttListBottomSheet(
            ottList = ottList,
            onDismiss = {},
            modifier = Modifier,
            sheetState = sheetState,
        )
    }
}
