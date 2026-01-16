package com.flint.domain.repository

import com.flint.domain.model.auth.SignupRequestModel
import com.flint.domain.model.auth.SignupResponseModel
import com.flint.domain.model.auth.SocialVerifyRequestModel
import com.flint.domain.model.auth.SocialVerifyResponseModel

interface AuthRepository {
    suspend fun signup(requestModel: SignupRequestModel): Result<SignupResponseModel>
    suspend fun socialVerify(requestModel: SocialVerifyRequestModel): Result<SocialVerifyResponseModel>
}
