package com.flint.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.flint.R

@Immutable
data class FlintTypography(
    val display1Sb32: TextStyle,
    val display1M32: TextStyle,
    val display2M28: TextStyle,
    val head1Sb22: TextStyle,
    val head1M22: TextStyle,
    val head2Sb20: TextStyle,
    val head2M20: TextStyle,
    val head3Sb18: TextStyle,
    val head3M18: TextStyle,
    val body1B16: TextStyle,
    val body1Sb16: TextStyle,
    val body1M16: TextStyle,
    val body1R16: TextStyle,
    val body2M14: TextStyle,
    val body2R14: TextStyle,
    val caption1M12: TextStyle,
    val caption1R12: TextStyle,
    val micro1M10: TextStyle,
)

val Typography =
    FlintTypography(
        display1Sb32 =
            flintTextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 32.sp,
                lineHeight = 1.5.em,
                letterSpacing = (-0.03).em,
            ),
        display1M32 =
            flintTextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 32.sp,
                lineHeight = 1.5.em,
                letterSpacing = (-0.03).em,
            ),
        display2M28 =
            flintTextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 28.sp,
                lineHeight = 1.5.em,
                letterSpacing = (-0.03).em,
            ),
        head1Sb22 =
            flintTextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                lineHeight = 1.3.em,
                letterSpacing = (-0.03).em,
            ),
        head1M22 =
            flintTextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp,
                lineHeight = 1.3.em,
                letterSpacing = (-0.03).em,
            ),
        head2Sb20 =
            flintTextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 1.3.em,
                letterSpacing = (-0.03).em,
            ),
        head2M20 =
            flintTextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                lineHeight = 1.3.em,
                letterSpacing = (-0.03).em,
            ),
        head3Sb18 =
            flintTextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 1.3.em,
                letterSpacing = (-0.03).em,
            ),
        head3M18 =
            flintTextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                lineHeight = 1.3.em,
                letterSpacing = (-0.03).em,
            ),
        body1B16 =
            flintTextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 1.4.em,
                letterSpacing = (-0.03).em,
            ),
        body1Sb16 =
            flintTextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 1.4.em,
                letterSpacing = (-0.03).em,
            ),
        body1M16 =
            flintTextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 1.4.em,
                letterSpacing = (-0.03).em,
            ),
        body1R16 =
            flintTextStyle(
                fontWeight = FontWeight.Regular,
                fontSize = 16.sp,
                lineHeight = 1.4.em,
                letterSpacing = (-0.03).em,
            ),
        body2M14 =
            flintTextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 1.3.em,
                letterSpacing = (-0.03).em,
            ),
        body2R14 =
            flintTextStyle(
                fontWeight = FontWeight.Regular,
                fontSize = 14.sp,
                lineHeight = 1.3.em,
                letterSpacing = (-0.03).em,
            ),
        caption1M12 =
            flintTextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 1.3.em,
                letterSpacing = (-0.03).em,
            ),
        caption1R12 =
            flintTextStyle(
                fontWeight = FontWeight.Regular,
                fontSize = 12.sp,
                lineHeight = 1.3.em,
                letterSpacing = (-0.03).em,
            ),
        micro1M10 =
            flintTextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                lineHeight = 1.3.em,
                letterSpacing = (-0.03).em,
            ),
    )

/** The default font weight - alias for [FontWeight.Normal] */
private val FontWeight.Companion.Regular: FontWeight get() = FontWeight.Normal

private fun flintTextStyle(
    fontWeight: FontWeight,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit,
    fontFamily: FontFamily = Pretendard,
    lineHeightStyle: LineHeightStyle = FigmaLineHeightStyle,
): TextStyle =
    TextStyle(
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight,
        lineHeightStyle = lineHeightStyle,
    )

private val Pretendard =
    FontFamily(
        Font(resId = R.font.pretendard_regular, weight = FontWeight.Regular),
        Font(resId = R.font.pretendard_medium, weight = FontWeight.Medium),
        Font(resId = R.font.pretendard_semi_bold, weight = FontWeight.SemiBold),
        Font(resId = R.font.pretendard_bold, weight = FontWeight.Bold),
    )

private val FigmaLineHeightStyle =
    LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None,
    )
