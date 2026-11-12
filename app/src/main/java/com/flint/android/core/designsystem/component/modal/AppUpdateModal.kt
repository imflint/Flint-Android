package com.flint.android.core.designsystem.component.modal

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.flint.android.R
import com.flint.android.core.designsystem.theme.FlintTheme

/**
 * 최신 버전으로 업데이트를 유도하는 안내 모달 (피그마 node 5746:5891)
 *
 * 디자인상 닫기 버튼이 없다. 강제 업데이트처럼 닫히면 안 되는 경우에는 [onDismiss] 로 빈 람다를 넘기면
 * 뒤로가기·바깥 영역 탭으로도 닫히지 않는다.
 * 버튼에서 스토어로 보내려면 [onUpdateClick] 에 `context::openPlayStore` 를 넘긴다.
 */
@Composable
fun AppUpdateModal(
    onUpdateClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OneButtonModal(
        title = "업데이트 안내",
        message = "더욱 편리하고 안정적인 서비스 이용을 위해\n최신 버전으로 업데이트해 주세요.",
        buttonText = "업데이트 하러가기",
        onConfirm = onUpdateClick,
        onDismiss = onDismiss,
        icon = R.drawable.ic_gradient_save,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun AppUpdateModalPreview() {
    FlintTheme {
        AppUpdateModal(
            onUpdateClick = {},
            onDismiss = {},
            modifier = Modifier.background(Color.White),
        )
    }
}
