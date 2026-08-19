package com.flint.android.presentation.home.sideeffect

import com.flint.android.domain.model.ott.OttListModel

interface HomeSideEffect {
    data class ShowOttListBottomSheet(val ottListModel: OttListModel): HomeSideEffect
}