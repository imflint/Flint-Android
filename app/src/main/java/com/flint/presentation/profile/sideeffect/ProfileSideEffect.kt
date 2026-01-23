package com.flint.presentation.profile.sideeffect

import com.flint.domain.model.ott.OttListModel

sealed interface ProfileSideEffect {
    data class ShowOttListBottomSheet(val ottListModel: OttListModel) : ProfileSideEffect
    data object WithdrawSuccess : ProfileSideEffect
}
