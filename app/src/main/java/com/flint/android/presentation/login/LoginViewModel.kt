package com.flint.android.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.android.core.analytics.AnalyticsTracker
import com.flint.android.core.analytics.FlintEvent
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
    private val authRepository: AuthRepository,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<UiState<LoginNavigationEvent>>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    /** 로그인 화면의 시작 버튼을 눌렀을 때. 성공 여부와 무관하게 시도 자체를 남긴다. */
    fun trackSignupClick() {
        analyticsTracker.track(FlintEvent.ClickSignup)
    }

    fun socialVerifyWithKakao(requestModel: SocialVerifyRequestModel) = viewModelScope.launch {
        _navigationEvent.emit(UiState.Loading)

        authRepository.socialVerify(requestModel)
            .onSuccess { data ->
                if (data.isRegistered) {
                    // 신규 가입자는 온보딩을 마쳐야 계정이 생기므로, 기존 사용자만 로그인 성공으로 집계한다.
                    analyticsTracker.setUserId(data.userId)
                    analyticsTracker.track(FlintEvent.CompleteLogin)
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