package com.flint.core.designsystem.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.theme.FlintTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlintBasicBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    brush = FlintTheme.colors.gradient700,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(52.dp)
                        .height(4.dp)
                        .background(
                            color = FlintTheme.colors.gray500,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    content()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun FlintBasicBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    FlintTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "BottomSheet 열기",
                modifier = Modifier
                    .align(Alignment.Center)
                    .noRippleClickable {
                        showSheet = true
                    }
            )

            val dismissCallback: () -> Unit = {
                scope.launch { sheetState.hide() }
                    .invokeOnCompletion { showSheet = false }
            }

            if (showSheet) {
                FlintBasicBottomSheet(
                    sheetState = sheetState,
                    onDismiss = dismissCallback
                ) {
                    Text("내용")
                    Button(onClick = {
                        dismissCallback()
                    }) {
                        Text("닫기")
                    }
                }
            }
        }
    }
}
