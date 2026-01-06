package com.flint.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorScale(
    val shade50: Color,
    val shade100: Color,
    val shade200: Color,
    val shade300: Color,
    val shade400: Color,
    val shade500: Color,
    val shade600: Color,
    val shade700: Color,
    val shade800: Color,
    val shade900: Color,
)

private val Primary =
    ColorScale(
        shade50 = Color(0xFFD7FAFF),
        shade100 = Color(0xFFAFF6FF),
        shade200 = Color(0xFF86EBFF),
        shade300 = Color(0xFF6ADAFF),
        shade400 = Color(0xFF1ABFF2),
        shade500 = Color(0xFF08A7DB),
        shade600 = Color(0xFF0780B8),
        shade700 = Color(0xFF005D86),
        shade800 = Color(0xFF084065),
        shade900 = Color(0xFF062845),
    )

private val Secondary =
    ColorScale(
        shade50 = Color(0xFFF0F1FF),
        shade100 = Color(0xFFD3D6FF),
        shade200 = Color(0xFFBBC0FF),
        shade300 = Color(0xFF8991FF),
        shade400 = Color(0xFF6B75FF),
        shade500 = Color(0xFF5D66EE),
        shade600 = Color(0xFF424BBD),
        shade700 = Color(0xFF2D3393),
        shade800 = Color(0xFF202573),
        shade900 = Color(0xFF0A0C45),
    )
private val Gray =
    ColorScale(
        shade50 = Color(0xFFF0F3F9),
        shade100 = Color(0xFFE6E9F1),
        shade200 = Color(0xFFCCD1DE),
        shade300 = Color(0xFFA6ADBF),
        shade400 = Color(0xFF7A8298),
        shade500 = Color(0xFF4F5669),
        shade600 = Color(0xFF3C4256),
        shade700 = Color(0xFF2B2F3A),
        shade800 = Color(0xFF21242C),
        shade900 = Color(0xFF151821),
    )

@Immutable
data class CommonColors(
    val white: Color,
    val background: Color,
    val error200: Color,
    val error500: Color,
    val error700: Color,
    val overlay: Color,
)

private val Common =
    CommonColors(
        white = Color(0xFFFFFFFF),
        background = Color(0xFF121212),
        error200 = Color(0xFFFFD7DB),
        error500 = Color(0xFFFF4D62),
        error700 = Color(0xFFB53746),
        overlay = Color(0xFF000000).copy(alpha = 0.6f),
    )

@Immutable
data class GradientColors(
    val gradient900: Brush,
    val gradient700: Brush,
    val gradient400: Brush,
    val imgBlur: Brush,
)

// TODO: start, end Offset을 설정해야 하는데, 학생 계정이라 못 함 ㅜㅜ
private val Gradients =
    GradientColors(
        gradient900 =
            Brush.linearGradient(
                colors = listOf(Color(0xFF3C4256), Color(0xFF121212)),
            ),
        gradient700 =
            Brush.linearGradient(
                colors = listOf(Color(0xFF3C4256), Color(0xFF2B2F3A)),
            ),
        gradient400 =
            Brush.linearGradient(
                colors = listOf(Color(0xFF1ABFF2), Color(0xFF86EBFF)),
            ),
        imgBlur =
            Brush.linearGradient(
                colors = listOf(Color(0xFF000000).copy(alpha = 0.8f), Color(0xFF000000)),
            ),
    )

@Immutable
data class FlintColors(
    val primary: ColorScale,
    val secondary: ColorScale,
    val gray: ColorScale,
    val common: CommonColors,
    val gradients: GradientColors,
)

val Colors =
    FlintColors(
        primary = Primary,
        secondary = Secondary,
        gray = Gray,
        common = Common,
        gradients = Gradients,
    )
