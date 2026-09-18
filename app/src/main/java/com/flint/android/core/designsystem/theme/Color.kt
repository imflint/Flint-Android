package com.flint.android.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * 투명 -> 불투명으로 잦아드는 그라데이션의 색 목록. [Brush.verticalGradient] 는 색을 균등 간격으로 배치한다.
 *
 * 알파를 선형으로 올리면 그라데이션이 시작되는 지점에서 기울기가 0에서 일정값으로 한 번에 꺾여
 * 가로줄처럼 보인다. 시작 기울기가 0인 2차 곡선(alpha = t*t)을 쓰면 시작점이 눈에 띄지 않는다.
 *
 * 끝에서는 [FADE_OPAQUE_AT] 지점에 미리 불투명해지도록 해서, 덮어야 할 경계에 원본이 남지 않게 한다.
 * 마지막까지 알파를 올리면 경계 직전에 원본이 몇 % 남아 오히려 잘린 자국이 보인다.
 */
private const val FADE_OPAQUE_AT = 11f / 12f

private fun easedFadeColors(
    color: Color,
    steps: Int = 12,
): List<Color> =
    List(steps + 1) { index ->
        val t = (index.toFloat() / steps / FADE_OPAQUE_AT).coerceAtMost(1f)
        color.copy(alpha = t * t)
    }

@Immutable
data class Colors(
    val primary50: Color,
    val primary100: Color,
    val primary200: Color,
    val primary300: Color,
    val primary400: Color,
    val primary500: Color,
    val primary600: Color,
    val primary700: Color,
    val primary800: Color,
    val primary900: Color,
    val secondary50: Color,
    val secondary100: Color,
    val secondary200: Color,
    val secondary300: Color,
    val secondary400: Color,
    val secondary500: Color,
    val secondary600: Color,
    val secondary700: Color,
    val secondary800: Color,
    val secondary900: Color,
    val gray50: Color,
    val gray100: Color,
    val gray200: Color,
    val gray300: Color,
    val gray400: Color,
    val gray500: Color,
    val gray600: Color,
    val gray700: Color,
    val gray800: Color,
    val gray900: Color,
    val white: Color,
    val background: Color,
    val subBackground: Color,
    val error200: Color,
    val error500: Color,
    val error700: Color,
    val overlay: Color,
    val spoilerBlur: Color,
    val gradient900: Brush,
    val gradient700: Brush,
    val gradient400: Brush,
    val gradient400Secondary: Brush,
    val imgBlur: Brush,
    val imgBlurHigh: Brush,
    val pink: Color,
    val green: Color,
    val orange: Color,
    val yellow: Color,
    val blue: Color,
    val kakao: Color,
    val transparent: Color,
    val buttonStroke: Brush,
    val myGradient: Brush,
    val blueGradient: Brush,
    val primary400Gradient: Brush,
    val grayGradient: Brush,
    val cardShadeGradient: Brush,
    val navbarGradient: Brush,
    val thumbnailGradient: Brush,
    val userBadgeGradient: Brush,
    val userBadgeStroke: Brush,
)

val FlintColors =
    Colors(
        primary50 = Color(0xFFD7FAFF),
        primary100 = Color(0xFFAFF6FF),
        primary200 = Color(0xFF86EBFF),
        primary300 = Color(0xFF6ADAFF),
        primary400 = Color(0xFF1ABFF2),
        primary500 = Color(0xFF08A7DB),
        primary600 = Color(0xFF0780B8),
        primary700 = Color(0xFF005D86),
        primary800 = Color(0xFF084065),
        primary900 = Color(0xFF062845),
        secondary50 = Color(0xFFF0F1FF),
        secondary100 = Color(0xFFD3D6FF),
        secondary200 = Color(0xFFBBC0FF),
        secondary300 = Color(0xFF8991FF),
        secondary400 = Color(0xFF6B75FF),
        secondary500 = Color(0xFF5D66EE),
        secondary600 = Color(0xFF424BBD),
        secondary700 = Color(0xFF2D3393),
        secondary800 = Color(0xFF202573),
        secondary900 = Color(0xFF0A0C45),
        gray50 = Color(0xFFF0F3F9),
        gray100 = Color(0xFFE6E9F1),
        gray200 = Color(0xFFCCD1DE),
        gray300 = Color(0xFFA6ADBF),
        gray400 = Color(0xFF7A8298),
        gray500 = Color(0xFF4F5669),
        gray600 = Color(0xFF3C4256),
        gray700 = Color(0xFF2B2F3A),
        gray800 = Color(0xFF21242C),
        gray900 = Color(0xFF151821),
        white = Color(0xFFFFFFFF),
        background = Color(0xFF121212),
        subBackground = Color(0xFF18191B),
        error200 = Color(0xFFFFD7DB),
        error500 = Color(0xFFFF4D62),
        error700 = Color(0xFFB53746),
        overlay = Color(0xFF000000).copy(alpha = 0.6f),
        spoilerBlur = Color(0xFF121212).copy(alpha = 0.3f),
        gradient900 = object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                return LinearGradientShader(
                    from = Offset(size.width * 0.12f, 0f),
                    to = Offset(size.width * 0.50f, size.height * 0.59f),
                    colors = listOf(Color(0xFF3C4256), Color(0xFF121212))
                )
            }
        },
        gradient700 = object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                return LinearGradientShader(
                    from = Offset.Zero,
                    to = Offset(size.width * 0.355f, size.height * 0.44f),
                    colors = listOf(Color(0xFF3C4256), Color(0xFF2B2F3A))
                )
            }
        },
        gradient400 = object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                return LinearGradientShader(
                    from = Offset.Zero,
                    to = Offset(size.width * 0.25f, size.height),
                    colors = listOf(Color(0xFF86EBFF), Color(0xFF1ABFF2))
                )
            }
        },
        gradient400Secondary =
            Brush.verticalGradient(
                colors = listOf(Color(0xFF8991FF), Color(0xFF6B75FF)),
            ),
        imgBlur =
            Brush.verticalGradient(
                colors = listOf(Color(0xFF000000).copy(0f), Color(0xFF000000).copy(alpha = 0.8f)),
            ),
        imgBlurHigh =
            Brush.verticalGradient(
                colors = listOf(Color(0xFF000000).copy(0f), Color(0xFF000000).copy(alpha = 0.4f)),
            ),
        pink = Color(0xFFFF76B6),
        green = Color(0xFF00BD76),
        orange = Color(0xFFFF744D),
        yellow = Color(0xFFF9B902),
        blue = Color(0xFF38A5FF),
        kakao = Color(0xFFFEE500),
        transparent = Color.Transparent,
        buttonStroke =
            Brush.verticalGradient(
                colors = listOf(Color(0xFFAEAEAE), Color(0xFF666666)),
            ),
        myGradient =
            Brush.verticalGradient(
                colors = listOf(Color(0xFF424BBD).copy(1f), Color(0xFF121212).copy(alpha = 0.04f)),
            ),
        blueGradient = Brush.verticalGradient(colors = easedFadeColors(Color(0xFF062845))),
        primary400Gradient = object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                return LinearGradientShader(
                    from = Offset(size.width * 0.5f, 0f),
                    to = Offset(size.width * 0.5f, size.height * 1.15f),
                    colors = listOf(Color(0xFF1ABFF2).copy(alpha = 0f), Color(0xFF1ABFF2).copy(alpha = 0.35f)),
                    colorStops = listOf(0.26f, 1f),
                )
            }
        },
        grayGradient = Brush.verticalGradient(colors = easedFadeColors(Color(0xFF21242C))),
        cardShadeGradient = object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                return LinearGradientShader(
                    from = Offset(size.width * 0.5f, 0f),
                    to = Offset(size.width * 0.5f, size.height * 1.15f),
                    colors = listOf(Color(0xFF2D4254).copy(alpha = 0f), Color(0xFF2D4254)),
                    colorStops = listOf(0.26f, 1f),
                )
            }
        },
        navbarGradient =
            Brush.verticalGradient(
                colors = listOf(Color(0xFF121212).copy(alpha = 1f), Color(0xFF121212).copy(alpha = 0f)),
            ),
        thumbnailGradient =
            Brush.verticalGradient(
                colors = listOf(Color(0xFF121212).copy(alpha = 1f), Color(0xFF121212).copy(alpha = 0.3f)),
            ),
        userBadgeGradient = object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                return LinearGradientShader(
                    from = Offset(size.width * 0.095f, size.height * 0.109f),
                    to = Offset(size.width * 0.921f, size.height * 0.844f),
                    colors = listOf(Color.White.copy(alpha = 0f), Color.White.copy(alpha = 0.1f)),
                )
            }
        },
        userBadgeStroke = object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                return LinearGradientShader(
                    from = Offset(size.width * 0.429f, 0f),
                    to = Offset(size.width * 0.738f, size.height),
                    colors = listOf(
                        Color.White.copy(alpha = 0.7f),
                        Color.White.copy(alpha = 0f),
                        Color.White.copy(alpha = 0.7f),
                    ),
                    colorStops = listOf(0f, 0.52f, 1f),
                )
            }
        },
    )

@Preview(device = Devices.DESKTOP)
@Composable
private fun FlintColorsPreview() {
    Column(
        modifier = Modifier.background(color = Color(0xFF313131)),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(text = "Primary", color = Color.White)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                Modifier
                    .background(color = FlintColors.primary50)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.primary100)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.primary200)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.primary300)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.primary400)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.primary500)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.primary600)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.primary700)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.primary800)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.primary900)
                    .size(100.dp),
            )
        }
        Text(text = "Secondary", color = Color.White)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                Modifier
                    .background(color = FlintColors.secondary50)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.secondary100)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.secondary200)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.secondary300)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.secondary400)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.secondary500)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.secondary600)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.secondary700)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.secondary800)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.secondary900)
                    .size(100.dp),
            )
        }
        Text(text = "GrayScale", color = Color.White)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                Modifier
                    .background(color = FlintColors.gray50)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.gray100)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.gray200)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.gray300)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.gray400)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.gray500)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.gray600)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.gray700)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.gray800)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.gray900)
                    .size(100.dp),
            )
        }
        Text("Common", color = Color.White)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                Modifier
                    .background(color = FlintColors.white)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.background)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.subBackground)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.error200)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.error500)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.error700)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.overlay)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.spoilerBlur)
                    .size(100.dp),
            )
        }

        Text("Gradient", color = Color.White)

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                Modifier
                    .background(brush = FlintColors.gradient900)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(brush = FlintColors.gradient700)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(brush = FlintColors.gradient400)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(brush = FlintColors.gradient400Secondary)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(brush = FlintColors.imgBlur)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(brush = FlintColors.imgBlurHigh)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(brush = FlintColors.buttonStroke)
                    .size(100.dp),
            )
        }

        Text("Keyword", color = Color.White)

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                Modifier
                    .background(color = FlintColors.pink)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.green)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.orange)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.yellow)
                    .size(100.dp),
            )
            Box(
                Modifier
                    .background(color = FlintColors.blue)
                    .size(100.dp),
            )
        }

        Text("ETC", color = Color.White)

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                Modifier
                    .background(color = FlintColors.kakao)
                    .size(100.dp),
            )
        }
    }
}
