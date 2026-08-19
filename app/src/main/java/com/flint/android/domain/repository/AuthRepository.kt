package com.flint.android.domain.repository

import com.flint.android.core.common.util.DataStoreKey.ACCESS_TOKEN
import com.flint.android.core.common.util.DataStoreKey.REFRESH_TOKEN
import com.flint.android.core.common.util.DataStoreKey.USER_ID
import com.flint.android.core.common.util.DataStoreKey.USER_NAME
import com.flint.android.data.local.PreferencesManager
import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.data.api.AuthApi
import com.flint.android.data.dto.auth.request.WithdrawRequestDto
import com.flint.android.domain.mapper.auth.toDto
import com.flint.android.domain.mapper.auth.toModel
import com.flint.android.domain.model.auth.SignupRequestModel
import com.flint.android.domain.model.auth.SignupResponseModel
import com.flint.android.domain.model.auth.SocialVerifyRequestModel
import com.flint.android.domain.model.auth.SocialVerifyResponseModel
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

    suspend fun logout(): Result<Unit> =
        suspendRunCatching {
            preferencesManager.clearAll()
        }

    // :TODO 일단 10으로 고정해둠 수정예정
    suspend fun withdraw(agreedTermsIds: List<String> = listOf("10")): Result<Unit> =
        suspendRunCatching {
            api.withdraw(WithdrawRequestDto(agreedTermsIds = agreedTermsIds))
            preferencesManager.clearAll()
        }
}