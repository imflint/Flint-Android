package com.flint.presentation.collectioncreate.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flint.core.designsystem.component.bottomsheet.MenuBottomSheet
import com.flint.core.designsystem.component.bottomsheet.MenuBottomSheetData
import com.flint.core.designsystem.theme.FlintTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionCreateBottomSheet() {
    val menuBottomSheetDataList = listOf(
        MenuBottomSheetData(
            label = "갤러리에서 선택",
            clickAction = {}
        ),
        MenuBottomSheetData(
            label = "커버 사진 삭제",
            color = FlintTheme.colors.error500,
            clickAction = {}
        )
    )

    val sheetState = rememberModalBottomSheetState()

    MenuBottomSheet(
        menuBottomSheetDataList = menuBottomSheetDataList,
        onDismiss = {},
        sheetState = sheetState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CollectionCreateBottomSheetPreview() {
    FlintTheme {
        CollectionCreateBottomSheet()
    }
}
