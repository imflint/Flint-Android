package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.ExplorationApi
import com.flint.domain.mapper.exploration.toModel
import com.flint.domain.model.exploration.ExplorationSessionModel
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
