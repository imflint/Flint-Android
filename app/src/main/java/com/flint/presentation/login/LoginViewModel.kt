package com.flint.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.UiState
import com.flint.domain.model.auth.SocialVerifyRequestModel
import com.flint.domain.repository.AuthRepository
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


sealed interface LoginNavigationEvent {
    data object NavigateToHome : LoginNavigationEvent
    data class NavigateToOnBoarding(val tempToken: String) : LoginNavigationEvent
}