package com.flint.data.repository

import com.flint.data.model.auth.SignupRequestModel
import com.flint.data.model.auth.SignupResponseModel
import com.flint.data.model.auth.SocialVerifyRequestModel
import com.flint.data.model.auth.SocialVerifyResponseModel

interface AuthRepository {
    suspend fun signup(requestModel: SignupRequestModel): Result<SignupResponseModel>

    suspend fun socialVerify(requestModel: SocialVerifyRequestModel): Result<SocialVerifyResponseModel>
}
