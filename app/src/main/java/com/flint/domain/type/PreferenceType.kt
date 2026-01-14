package com.flint.domain.type

import androidx.annotation.DrawableRes
import com.flint.R

sealed class KeywordType {
    data object Small : KeywordType()

    data class Large(
        val preferenceType: PreferenceType,
    ) : KeywordType()
}

enum class PreferenceType(
    @DrawableRes val backgroundRes: Int,
) {
    Green(R.drawable.bg_tag_green),
    Orange(R.drawable.bg_tag_orange),
    Yellow(R.drawable.bg_tag_yellow),
    Blue(R.drawable.bg_tag_blue),
    Pink(R.drawable.bg_tag_pink),
}
