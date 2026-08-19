package com.flint.android.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.android.core.common.util.DataStoreKey.USER_NAME
import com.flint.android.data.local.PreferencesManager
import com.flint.android.domain.repository.AuthRepository
import com.flint.android.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    private val _navigateToLogin = MutableSharedFlow<Unit>()
    val navigateToLogin = _navigateToLogin.asSharedFlow()

    fun loadUserInfo() {
        viewModelScope.launch {
            val nickname = preferencesManager.getString(USER_NAME).first()
            _uiState.update { it.copy(nickname = nickname) }
        }

        viewModelScope.launch {
            userRepository.getUserProfile(userId = null)
                .onSuccess { profile ->
                    _uiState.update {
                        it.copy(
                            profileImageUrl = profile.profileImageUrl,
                            email = profile.email,
                        )
                    }
                }
                .onFailure { Timber.e(it) }
        }
    }

    fun showLogoutDialog() {
        _uiState.update { it.copy(isLogoutDialogVisible = true) }
    }

    fun dismissLogoutDialog() {
        _uiState.update { it.copy(isLogoutDialogVisible = false) }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
                .onSuccess { _navigateToLogin.emit(Unit) }
                .onFailure { Timber.e(it) }
        }
    }

}
