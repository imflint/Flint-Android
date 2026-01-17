package com.flint.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.datastore.PreferencesManager
import com.flint.core.common.util.DataStoreKey.ACCESS_TOKEN
import com.flint.core.common.util.DataStoreKey.REFRESH_TOKEN
import com.flint.core.common.util.DataStoreKey.USER_ID
import com.flint.core.common.util.UiState
import com.flint.data.model.auth.SocialVerifyRequestModel
import com.flint.domain.repository.AuthRepository
import com.flint.presentation.login.data.VerifyStatusData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceManager: PreferencesManager,
) : ViewModel() {

    private val _verifyStatus = MutableStateFlow<UiState<VerifyStatusData>>(UiState.Empty) // 로그인 여부, tempToken(회원가입 용)
    val verifyStatus = _verifyStatus.asStateFlow()

    fun socialVerifyWithKakao(requestModel: SocialVerifyRequestModel) = viewModelScope.launch {
        authRepository.socialVerify(requestModel).fold(
            onSuccess = { result ->
                if (result.isRegistered) {
                    result.accessToken?.let { preferenceManager.saveString(ACCESS_TOKEN, it) }
                    result.refreshToken?.let { preferenceManager.saveString(REFRESH_TOKEN, it) }
                    result.userId?.let { preferenceManager.saveString(USER_ID, it.toString()) }
                }

                _verifyStatus.emit(UiState.Success(VerifyStatusData(result.isRegistered, result.tempToken)))
            },
            onFailure = {
                _verifyStatus.emit(UiState.Failure)
            }
        )
    }

}