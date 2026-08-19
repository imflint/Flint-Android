package com.flint.android.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.android.core.common.util.UiState
import com.flint.android.domain.model.auth.SocialVerifyRequestModel
import com.flint.android.domain.repository.AuthRepository
import com.flint.android.presentation.login.event.LoginNavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<UiState<LoginNavigationEvent>>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun socialVerifyWithKakao(requestModel: SocialVerifyRequestModel) = viewModelScope.launch {
        _navigationEvent.emit(UiState.Loading)

        authRepository.socialVerify(requestModel)
            .onSuccess { data ->
                if (data.isRegistered) {
                    _navigationEvent.emit(UiState.Success(LoginNavigationEvent.NavigateToHome))
                } else {
                    _navigationEvent.emit(UiState.Success(LoginNavigationEvent.NavigateToOnBoarding(data.tempToken ?: "")))
                }
            }
            .onFailure {
                _navigationEvent.emit(UiState.Failure)
            }
    }
}