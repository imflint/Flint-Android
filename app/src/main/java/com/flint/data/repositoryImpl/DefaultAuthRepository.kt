package com.flint.data.repositoryImpl

import com.flint.data.api.AuthApi
import com.flint.data.mapper.auth.toDto
import com.flint.data.mapper.auth.toModel
import com.flint.domain.model.auth.SignupRequestModel
import com.flint.domain.model.auth.SignupResponseModel
import com.flint.domain.model.auth.SocialVerifyRequestModel
import com.flint.domain.model.auth.SocialVerifyResponseModel
import com.flint.domain.repository.AuthRepository
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val apiService: AuthApi,
) : AuthRepository {

    override suspend fun signup(requestModel: SignupRequestModel): Result<SignupResponseModel> =
        runCatching {
            apiService.signup(requestModel.toDto()).data.toModel()
        }

    override suspend fun socialVerify(requestModel: SocialVerifyRequestModel): Result<SocialVerifyResponseModel> =
        runCatching {
            apiService.socialVerify(requestModel.toDto()).data.toModel()
        }
}
