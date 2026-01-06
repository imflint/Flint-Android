package com.flint.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalFlintColors =
    staticCompositionLocalOf<FlintColors> { error("No FlintColors provided") }

object FlintTheme {
    val colors: FlintColors
        @Composable
        get() = LocalFlintColors.current
}

@Composable
fun FlintTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalFlintColors provides Colors,
    ) {
        content()
    }
}
