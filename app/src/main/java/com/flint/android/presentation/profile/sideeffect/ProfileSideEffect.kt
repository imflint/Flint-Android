package com.flint.android.presentation.profile.sideeffect

import com.flint.android.domain.model.ott.OttListModel

sealed interface ProfileSideEffect {
    data class ShowOttListBottomSheet(val ottListModel: OttListModel) : ProfileSideEffect
}
