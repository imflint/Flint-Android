package com.flint.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.flint.R
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun FlintBackTopAppbar(
    // 뒤로가기 버튼이 있는 이상 뒤로 움직이는 이벤트는 무조건 있음. 그래서 필수 인자
    onClick: () -> Unit,
    // 밖에서 주입해주기 위해 / 책임을 밖으로 빼기 위해
    modifier: Modifier = Modifier,
    backgroundColor: Color = FlintTheme.colors.background,
    title: String = "",
    closeable: Boolean = false,
    actionText: String = "",
    textColor: Color = Color.Unspecified
) {
    FlintBasicTopAppbar(
        modifier = modifier,
        backgroundColor = backgroundColor,
        navigationIcon = {
            Icon(
                modifier = Modifier.noRippleClickable(onClick = onClick),
                imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                contentDescription = null,
                tint = FlintTheme.colors.white
            )
        },
        title = title,
        action = {
            if (closeable) {
                Icon(
                    modifier = Modifier,
                    imageVector = ImageVector.vectorResource(R.drawable.ic_cancel),
                    contentDescription = null,
                    tint = FlintTheme.colors.white
                )
            } else {
                Text(
                    text = actionText,
                    color = textColor,
                    style = FlintTheme.typography.body1M16
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun FlintBackTopAppbarPreview() {
    FlintTheme {
        Column {
            FlintBackTopAppbar(
                onClick = {}
            )

            FlintBackTopAppbar(
                onClick = {},
                title = "전체 컬렉션"
            )

            FlintBackTopAppbar(
                onClick = {},
                title = "전체 컬렉션",
                closeable = true
            )

            FlintBackTopAppbar(
                onClick = {},
                title = "전체 컬렉션",
                actionText = "뒤로가기",
                textColor = FlintTheme.colors.error700
            )
        }
    }
}
