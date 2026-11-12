package com.flint.android.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.android.BuildConfig
import com.flint.android.core.analytics.AnalyticsTracker
import com.flint.android.core.common.util.DataStoreKey.ACCESS_TOKEN
import com.flint.android.core.common.util.DataStoreKey.USER_ID
import com.flint.android.core.common.util.isUpdateRequired
import com.flint.android.data.local.PreferencesManager
import com.flint.android.domain.repository.SystemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject

sealed interface AutoLoginState {
    data object Loading : AutoLoginState
    data object NavigateToHome : AutoLoginState
    data object NavigateToLogin : AutoLoginState
}

sealed interface AppUpdateState {
    data object Checking : AppUpdateState
    data object NotRequired : AppUpdateState
    data class Required(val storeUrl: String) : AppUpdateState
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val analyticsTracker: AnalyticsTracker,
    private val systemRepository: SystemRepository,
) : ViewModel() {

    private val _autoLoginState = MutableStateFlow<AutoLoginState>(AutoLoginState.Loading)
    val autoLoginState: StateFlow<AutoLoginState> = _autoLoginState.asStateFlow()

    private val _appUpdateState = MutableStateFlow<AppUpdateState>(AppUpdateState.Checking)
    val appUpdateState: StateFlow<AppUpdateState> = _appUpdateState.asStateFlow()

    init {
        checkAutoLogin()
        checkAppUpdate()
    }

    /**
     * 현재 버전이 서버의 최소 지원 버전보다 낮으면 강제 업데이트 대상으로 본다.
     *
     * 조회가 실패하면(정책 미등록 404 포함) 그냥 통과시킨다 — 서버 장애로 앱 전체가 막히지 않게 하자는 서버와의 계약.
     * 응답이 늦어도 스플래시가 붙잡혀 있지 않도록 시간 제한을 두고, 넘기면 요청을 취소하고 통과시킨다.
     */
    private fun checkAppUpdate() = viewModelScope.launch {
        val appVersion = withTimeoutOrNull(APP_VERSION_TIMEOUT_MILLIS) {
            systemRepository.getAppVersion()
                .onFailure { Timber.w(it, "최소 지원 버전 조회 실패 — 업데이트 확인 없이 통과") }
                .getOrNull()
        }

        _appUpdateState.value =
            if (appVersion != null && isUpdateRequired(BuildConfig.VERSION_NAME, appVersion.minimumVersion)) {
                AppUpdateState.Required(storeUrl = appVersion.storeUrl)
            } else {
                AppUpdateState.NotRequired
            }
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

    private companion object {
        const val APP_VERSION_TIMEOUT_MILLIS = 3_000L
    }
}
