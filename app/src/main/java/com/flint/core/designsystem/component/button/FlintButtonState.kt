package com.flint.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.theme.FlintTheme

enum class FlintButtonState(
    val enabled: Boolean
) {
    Able(enabled = true) {
        override val background: Brush
            @Composable
            @ReadOnlyComposable
            get() = FlintTheme.colors.gradient400

        override val contentColor: Color
            @Composable
            @ReadOnlyComposable
            get() = FlintTheme.colors.white

        override val border: BorderStroke?
            @Composable
            @ReadOnlyComposable
            get() = null
    },

    Disable(enabled = false) {
        override val background: Brush
            @Composable
            @ReadOnlyComposable
            get() = SolidColor(FlintTheme.colors.gray700)

        override val contentColor: Color
            @Composable
            @ReadOnlyComposable
            get() = FlintTheme.colors.gray400

        override val border: BorderStroke?
            @Composable
            @ReadOnlyComposable
            get() = null
    },

    Outline(enabled = true) {
        override val background: Brush
            @Composable
            @ReadOnlyComposable
            get() = SolidColor(FlintTheme.colors.gray800)

        override val contentColor: Color
            @Composable
            @ReadOnlyComposable
            get() = FlintTheme.colors.white

        override val border: BorderStroke
            @Composable
            @ReadOnlyComposable
            get() = BorderStroke(2.dp, FlintTheme.colors.gray500)
    },

    ColorOutline(enabled = true) {
        override val background: Brush
            @Composable
            @ReadOnlyComposable
            get() = SolidColor(FlintTheme.colors.gray800)

        override val contentColor: Color
            @Composable
            @ReadOnlyComposable
            get() = FlintTheme.colors.white

        override val border: BorderStroke
            @Composable
            @ReadOnlyComposable
            get() = BorderStroke(2.dp, FlintTheme.colors.primary400)
    }
    ;

    abstract val background: Brush
        @Composable
        @ReadOnlyComposable
        get

    abstract val contentColor: Color
        @Composable
        @ReadOnlyComposable
        get

    abstract val border: BorderStroke?
        @Composable
        @ReadOnlyComposable
        get
}
