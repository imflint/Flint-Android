package com.flint.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.DataStoreKey.ACCESS_TOKEN
import com.flint.data.local.PreferencesManager
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
                AutoLoginState.NavigateToHome
            } else {
                AutoLoginState.NavigateToLogin
            }
        } catch (e: Exception) {
            AutoLoginState.NavigateToLogin
        }
    }
}
