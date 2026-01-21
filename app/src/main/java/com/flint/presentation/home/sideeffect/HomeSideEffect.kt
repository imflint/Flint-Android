package com.flint.presentation.home.sideeffect

import com.flint.domain.model.ott.OttListModel

interface HomeSideEffect {
    data class ShowOttListBottomSheet(val ottListModel: OttListModel): HomeSideEffect
}