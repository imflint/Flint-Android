package com.flint.android.domain.repository

import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.data.api.SystemApi
import com.flint.android.domain.mapper.system.toModel
import com.flint.android.domain.model.system.AppVersionModel
import javax.inject.Inject

class SystemRepository @Inject constructor(
    private val api: SystemApi,
) {
    suspend fun getAppVersion(): Result<AppVersionModel> =
        suspendRunCatching {
            api.getAppVersion(platform = PLATFORM_ANDROID).data.toModel()
        }

    private companion object {
        const val PLATFORM_ANDROID = "ANDROID"
    }
}
