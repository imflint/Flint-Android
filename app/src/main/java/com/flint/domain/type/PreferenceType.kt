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
    GREEN(
        color = FlintColors.green,
        backgroundRes = R.drawable.bg_tag_green,
    ),
    ORANGE(
        color = FlintColors.orange,
        backgroundRes = R.drawable.bg_tag_orange,
    ),
    YELLOW(
        color = FlintColors.yellow,
        backgroundRes = R.drawable.bg_tag_yellow,
    ),
    BLUE(
        color = FlintColors.blue,
        backgroundRes = R.drawable.bg_tag_blue,
    ),
    PINK(
        color = FlintColors.pink,
        backgroundRes = R.drawable.bg_tag_pink,
    ),
}
