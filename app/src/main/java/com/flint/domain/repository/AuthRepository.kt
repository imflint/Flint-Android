package com.flint.domain.repository

import com.flint.core.common.util.DataStoreKey.ACCESS_TOKEN
import com.flint.core.common.util.DataStoreKey.USER_ID
import com.flint.data.local.PreferencesManager
import com.flint.core.common.util.DataStoreKey.USER_NAME
import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.AuthApi
import com.flint.domain.mapper.auth.toDto
import com.flint.domain.mapper.auth.toModel
import com.flint.domain.model.auth.SignupRequestModel
import com.flint.domain.model.auth.SignupResponseModel
import com.flint.domain.model.auth.SocialVerifyRequestModel
import com.flint.domain.model.auth.SocialVerifyResponseModel
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val preferencesManager: PreferencesManager
) {
    suspend fun signup(model: SignupRequestModel): Result<SignupResponseModel> =
        suspendRunCatching { api.signup(model.toDto()).data.toModel() }

    suspend fun socialVerify(model: SocialVerifyRequestModel): Result<SocialVerifyResponseModel> {
        val result = api.socialVerify(model.toDto()).data.toModel()
        preferencesManager.saveString(ACCESS_TOKEN, result.accessToken.toString())
        preferencesManager.saveString(USER_NAME, result.nickName.toString())
        preferencesManager.saveString(USER_ID, result.userId.toString())

        return suspendRunCatching { result }
    }

}