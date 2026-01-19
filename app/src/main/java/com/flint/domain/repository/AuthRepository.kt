package com.flint.domain.repository

import com.flint.data.api.AuthApi
import com.flint.domain.mapper.auth.toDto
import com.flint.domain.mapper.auth.toModel
import com.flint.domain.model.auth.SignupRequestModel
import com.flint.domain.model.auth.SignupResponseModel
import com.flint.domain.model.auth.SocialVerifyRequestModel
import com.flint.domain.model.auth.SocialVerifyResponseModel
import javax.inject.Inject

class AuthRepository
    @Inject
    constructor(
        private val api: AuthApi,
    ) {
        suspend fun signup(model: SignupRequestModel): Result<SignupResponseModel> =
            runCatching { api.signup(model.toDto()).data.toModel() }

        suspend fun socialVerify(model: SocialVerifyRequestModel): Result<SocialVerifyResponseModel> =
            runCatching { api.socialVerify(model.toDto()).data.toModel() }
    }
