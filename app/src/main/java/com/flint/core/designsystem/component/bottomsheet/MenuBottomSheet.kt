package com.flint.core.designsystem.component.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.theme.FlintTheme

data class MenuBottomSheetData(
    val label: String,
    val color: Color = Color.White,
    val clickAction: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBottomSheet(
    menuBottomSheetDataList: List<MenuBottomSheetData>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    FlintBasicBottomSheet(
        sheetState = sheetState,
        onDismiss = onDismiss,
        modifier = modifier,
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
        ) {
            itemsIndexed(menuBottomSheetDataList) { index, item ->
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .noRippleClickable {
                                item.clickAction()
                                onDismiss()
                            },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Spacer(Modifier.weight(1f))

                    Text(
                        text = item.label,
                        color = item.color,
                        style = FlintTheme.typography.body1M16,
                    )

                    Spacer(Modifier.weight(1f))

                    if (index != menuBottomSheetDataList.size - 1) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = FlintTheme.colors.gray500,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewMenuBottomSheet() {
    FlintTheme {
        val menuBottomSheetDataList =
            listOf(
                MenuBottomSheetData(
                    label = "갤러리에서 선택",
                    clickAction = {},
                ),
                MenuBottomSheetData(
                    label = "프로필 사진 삭제",
                    color = FlintTheme.colors.error500,
                    clickAction = {},
                ),
            )

        val sheetState = rememberModalBottomSheetState()

        MenuBottomSheet(
            menuBottomSheetDataList = menuBottomSheetDataList,
            onDismiss = {},
            sheetState = sheetState,
        )
    }
}
