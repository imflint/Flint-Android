package com.flint.domain.model.auth

data class SignupRequestModel(
    val tempToken: String,
    val nickname: String,
    val favoriteContentIds: List<String>,
    val agreedTermsIds: List<String>,
)

data class SignupResponseModel(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long,
)
