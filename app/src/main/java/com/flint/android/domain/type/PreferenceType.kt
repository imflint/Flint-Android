package com.flint.android.domain.type

import androidx.compose.ui.graphics.Color
import com.flint.android.core.designsystem.theme.FlintColors

sealed class KeywordType {
    data object Small : KeywordType()

    data class Large(
        val preferenceType: PreferenceType,
    ) : KeywordType()
}

enum class PreferenceType(
    val color: Color,
) {
    GREEN(color = FlintColors.green),
    ORANGE(color = FlintColors.orange),
    YELLOW(color = FlintColors.yellow),
    BLUE(color = FlintColors.blue),
    PINK(color = FlintColors.pink),
}
