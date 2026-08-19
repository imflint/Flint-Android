package com.flint.android.core.designsystem.component.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flint.android.R
import com.flint.android.core.designsystem.theme.FlintTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShowToast(
    text: String,
    imageVector: ImageVector?,
    paddingValues: PaddingValues,
    yOffset: Dp,
    hide: () -> Unit,
    imeYOffset: Dp = yOffset,
    key: Any = text,
) {
    // 호출부가 `if (show) ShowToast(...)` 형태라 hide() 가 불리는 순간 컴포지션에서 빠져
    // 퇴장 애니메이션을 그릴 수 없다. 그래서 퇴장 애니메이션을 먼저 재생하고 끝난 뒤에 hide() 를 부른다.
    var visible by remember { mutableStateOf(false) }

    // key 가 바뀌면(같은 문구를 다시 띄우는 경우 포함) 타이머와 등장 애니메이션을 처음부터 다시 돌린다.
    LaunchedEffect(key) {
        visible = true
        delay(TOAST_VISIBLE_MILLIS)
        visible = false
        delay(TOAST_EXIT_MILLIS.toLong())
        hide()
    }

    val bottomOffset = if (WindowInsets.isImeVisible) imeYOffset else yOffset

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = toastEnterTransition(),
            exit = toastExitTransition(),
            modifier = Modifier
                .padding(paddingValues)
                .padding(bottom = bottomOffset),
        ) {
            FlintToast(
                text = text,
                imageVector = imageVector,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShowToastPreview() {
    FlintTheme {
        var show: Boolean by remember { mutableStateOf(true) }

        if (show) {
            ShowToast(
                text = "저장되었습니다",
                imageVector = ImageVector.vectorResource(R.drawable.ic_check),
                paddingValues = PaddingValues.Zero,
                yOffset = 80.dp,
                hide = { show = false },
            )
        }
    }
}

@Preview
@Composable
private fun ShowToastWithoutIconPreview() {
    FlintTheme {
        var show: Boolean by remember { mutableStateOf(true) }

        if (show) {
            ShowToast(
                text = "알림 메시지입니다",
                imageVector = null,
                paddingValues = PaddingValues.Zero,
                yOffset = 80.dp,
                hide = { show = false },
            )
        }
    }
}

internal const val TOAST_VISIBLE_MILLIS = 2000L
internal const val TOAST_ENTER_MILLIS = 220
internal const val TOAST_EXIT_MILLIS = 180

/** 아래에서 살짝 올라오며 나타난다. */
internal fun toastEnterTransition() =
    fadeIn(animationSpec = tween(TOAST_ENTER_MILLIS)) +
        slideInVertically(animationSpec = tween(TOAST_ENTER_MILLIS)) { height -> height / 2 }

/** 나타날 때보다 짧게, 조금만 내려가며 사라진다. */
internal fun toastExitTransition() =
    fadeOut(animationSpec = tween(TOAST_EXIT_MILLIS)) +
        slideOutVertically(animationSpec = tween(TOAST_EXIT_MILLIS)) { height -> height / 4 }
