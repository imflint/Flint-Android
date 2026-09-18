package com.flint.android.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.android.core.analytics.AnalyticsTracker
import com.flint.android.core.common.util.DataStoreKey.ACCESS_TOKEN
import com.flint.android.core.common.util.DataStoreKey.USER_ID
import com.flint.android.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AutoLoginState {
    data object Loading : AutoLoginState
    data object NavigateToHome : AutoLoginState
    data object NavigateToLogin : AutoLoginState
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {

    private val _autoLoginState = MutableStateFlow<AutoLoginState>(AutoLoginState.Loading)
    val autoLoginState: StateFlow<AutoLoginState> = _autoLoginState.asStateFlow()

    init {
        checkAutoLogin()
    }

    private fun checkAutoLogin() = viewModelScope.launch {
        _autoLoginState.value = try {
            val accessToken = preferencesManager.getString(ACCESS_TOKEN).first()
            if (accessToken.isNotEmpty()) {
                identifyUser()
                AutoLoginState.NavigateToHome
            } else {
                AutoLoginState.NavigateToLogin
            }
        } catch (e: Exception) {
            AutoLoginState.NavigateToLogin
        }
    }

    /**
     * 자동 로그인으로 들어온 사용자를 분석 도구에 다시 식별시킨다.
     *
     * 식별은 로그인·회원가입 시점에만 하고 SDK 가 그 값을 저장해두지만,
     * 앱 데이터가 지워졌거나 계측이 없던 버전에서 올라온 기기는 저장된 식별자가 없다.
     * 그 상태로 자동 로그인하면 다시 로그인할 때까지 모든 이벤트가 익명으로 쌓인다.
     */
    private suspend fun identifyUser() {
        val userId = runCatching { preferencesManager.getString(USER_ID).first() }.getOrNull()
        if (!userId.isNullOrEmpty()) analyticsTracker.setUserId(userId)
    }
}
