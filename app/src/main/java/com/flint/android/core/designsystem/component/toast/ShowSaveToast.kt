package com.flint.android.core.designsystem.component.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flint.android.core.designsystem.component.snackbar.SaveToast
import com.flint.android.core.designsystem.theme.FlintTheme
import kotlinx.coroutines.delay

@Composable
fun ShowSaveToast(
    navigateToSavedCollection: () -> Unit,
    paddingValues: PaddingValues,
    yOffset: Dp,
    hide: () -> Unit,
) {
    // 퇴장 애니메이션을 그리려면 hide() 를 애니메이션 뒤로 미뤄야 한다. ShowToast 와 같은 방식.
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(TOAST_VISIBLE_MILLIS)
        visible = false
        delay(TOAST_EXIT_MILLIS.toLong())
        hide()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = toastEnterTransition(),
            exit = toastExitTransition(),
            modifier = Modifier
                .padding(paddingValues)
                .padding(bottom = yOffset),
        ) {
            SaveToast(navigateToSavedCollection = navigateToSavedCollection)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShowSaveToastPreview() {
    FlintTheme {
        var show: Boolean by remember { mutableStateOf(true) }

        if (show) {
            ShowSaveToast(
                navigateToSavedCollection = {},
                paddingValues = PaddingValues.Zero,
                yOffset = 80.dp,
                hide = { show = false },
            )
        }
    }
}
