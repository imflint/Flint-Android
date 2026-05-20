package com.flint.presentation.setting.withdraw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class WithdrawUiState(
    val isConfirmed: Boolean = false,
    val isLoading: Boolean = false,
)

@HiltViewModel
class WithdrawViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WithdrawUiState())
    val uiState: StateFlow<WithdrawUiState> = _uiState.asStateFlow()

    private val _navigateToLogin = MutableSharedFlow<Unit>()
    val navigateToLogin = _navigateToLogin.asSharedFlow()

    fun toggleConfirm() {
        _uiState.update { it.copy(isConfirmed = !it.isConfirmed) }
    }

    fun withdraw() {
        if (!_uiState.value.isConfirmed) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.withdraw()
                .onSuccess { _navigateToLogin.emit(Unit) }
                .onFailure {
                    Timber.e(it)
                    _uiState.update { state -> state.copy(isLoading = false) }
                }
        }
    }
}
