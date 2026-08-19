package com.flint.android.core.designsystem.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.flint.android.core.designsystem.interaction.FlintPressDefaults

@Composable
fun FlintTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalColors provides FlintColors,
        LocalTypography provides FlintTypography,
        // MaterialTheme 을 쓰지 않아서 LocalIndication 이 Foundation 기본값(디버그용 검정 30% 오버레이)이었다.
        // 여기서 덮어 두면 기존 clickable 들도 별도 수정 없이 Flint 규격 딤을 쓴다.
        LocalIndication provides FlintPressDefaults.dimIndication,
    ) {
        content()
    }
}

object FlintTheme {
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}

private val LocalColors =
    staticCompositionLocalOf<Colors> { error("No Colors provided") }

private val LocalTypography =
    staticCompositionLocalOf<Typography> { error("No Typography provided") }
