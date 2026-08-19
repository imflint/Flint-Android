package com.flint.android.domain.repository

import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.data.api.ExplorationApi
import com.flint.android.domain.mapper.exploration.toModel
import com.flint.android.domain.model.exploration.ExplorationSessionModel
import javax.inject.Inject

class ExplorationRepository @Inject constructor(
    private val apiService: ExplorationApi,
) {
    suspend fun getExplorationSession(): Result<ExplorationSessionModel> =
        suspendRunCatching {
            apiService.getExplorationSession().data.toModel()
        }

    suspend fun advanceToNextExplorationSession(): Result<ExplorationSessionModel> =
        suspendRunCatching {
            apiService.advanceToNextExplorationSession().data.toModel()
        }
}
