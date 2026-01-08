package com.flint.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun FlintTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalColors provides FlintColors,
        LocalFlintTypography provides Typography,
    ) {
        content()
    }
}

object FlintTheme {
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: FlintTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalFlintTypography.current
}

private val LocalColors =
    staticCompositionLocalOf<Colors> { error("No Colors provided") }

private val LocalFlintTypography =
    staticCompositionLocalOf<FlintTypography> { error("No FlintTypography provided") }
