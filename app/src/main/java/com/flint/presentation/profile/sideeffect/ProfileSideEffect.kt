package com.flint.presentation.profile.sideeffect

import com.flint.domain.model.ott.OttListModel

interface ProfileSideEffect {
    data class ShowOttListBottomSheet(val ottListModel: OttListModel) : ProfileSideEffect
}
