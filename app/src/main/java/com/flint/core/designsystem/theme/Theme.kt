package com.flint.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun FlintTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalFlintColors provides Colors,
        LocalFlintTypography provides Typography,
    ) {
        content()
    }
}

object FlintTheme {
    val colors: FlintColors
        @Composable
        @ReadOnlyComposable
        get() = LocalFlintColors.current

    val typography: FlintTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalFlintTypography.current
}

private val LocalFlintColors =
    staticCompositionLocalOf<FlintColors> { error("No FlintColors provided") }

private val LocalFlintTypography =
    staticCompositionLocalOf<FlintTypography> { error("No FlintTypography provided") }
