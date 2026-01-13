package com.flint.domain.type

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.flint.R
import com.flint.core.designsystem.theme.FlintColors

sealed class KeywordType {
    data object Small : KeywordType()

    data class Large(
        val preferenceType: PreferenceType,
    ) : KeywordType()
}

enum class PreferenceType(
    val color: Color,
    @DrawableRes val backgroundRes: Int,
) {
    Green(
        color = FlintColors.green,
        backgroundRes = R.drawable.bg_tag_green,
    ),
    Orange(
        color = FlintColors.orange,
        backgroundRes = R.drawable.bg_tag_orange,
    ),
    Yellow(
        color = FlintColors.yellow,
        backgroundRes = R.drawable.bg_tag_yellow,
    ),
    Blue(
        color = FlintColors.blue,
        backgroundRes = R.drawable.bg_tag_blue,
    ),
    Pink(
        color = FlintColors.pink,
        backgroundRes = R.drawable.bg_tag_pink,
    ),
}
