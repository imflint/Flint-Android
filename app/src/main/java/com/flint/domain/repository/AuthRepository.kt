package com.flint.domain.repository

import com.flint.core.common.util.DataStoreKey.ACCESS_TOKEN
import com.flint.core.common.util.DataStoreKey.REFRESH_TOKEN
import com.flint.core.common.util.DataStoreKey.USER_ID
import com.flint.core.common.util.DataStoreKey.USER_NAME
import com.flint.data.local.PreferencesManager
import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.AuthApi
import com.flint.domain.mapper.auth.toDto
import com.flint.domain.mapper.auth.toModel
import com.flint.domain.model.auth.SignupRequestModel
import com.flint.domain.model.auth.SignupResponseModel
import com.flint.domain.model.auth.SocialVerifyRequestModel
import com.flint.domain.model.auth.SocialVerifyResponseModel
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val preferencesManager: PreferencesManager
) {
    suspend fun signup(model: SignupRequestModel): Result<SignupResponseModel> =
        suspendRunCatching {
            val result = api.signup(model.toDto()).data.toModel()
            preferencesManager.saveString(ACCESS_TOKEN, result.accessToken)
            preferencesManager.saveString(REFRESH_TOKEN, result.refreshToken)
            preferencesManager.saveString(USER_ID, result.userId.toString())
            preferencesManager.saveString(USER_NAME, model.nickname)
            result
        }

    suspend fun socialVerify(model: SocialVerifyRequestModel): Result<SocialVerifyResponseModel> =
        suspendRunCatching {
            val result = api.socialVerify(model.toDto()).data.toModel()
            result.accessToken?.let { preferencesManager.saveString(ACCESS_TOKEN, it) }
            result.userId?.let { preferencesManager.saveString(USER_ID, it) }
            result.nickName?.let { preferencesManager.saveString(USER_NAME, it) }
            result
        }

}