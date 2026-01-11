package com.flint.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun FlintTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalColors provides FlintColors,
        LocalTypography provides FlintTypography
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
