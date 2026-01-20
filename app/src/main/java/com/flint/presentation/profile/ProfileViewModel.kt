package com.flint.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.extension.updateSuccess
import com.flint.core.common.util.UiState
import com.flint.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ProfileUiState>>(
        UiState.Success(ProfileUiState.Fake)
    )
    val uiState: StateFlow<UiState<ProfileUiState>> = _uiState.asStateFlow()

    private val tempUserId = "801159854933808613"

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            getProfile()
        }
    }

    private fun getProfile() {
        viewModelScope.launch {
            userRepository.getUserProfile(userId = tempUserId)
                .onSuccess { myInfo ->
                    Timber.d("getProfile success: $myInfo")
                    _uiState.updateSuccess {
                        it.copy(
                            profile = myInfo
                        )
                    }
                }
                .onFailure {
                    Timber.d("getProfile failure: $it")
                }
        }
    }

    fun refreshProfileKeyword() {
        viewModelScope.launch {
            userRepository.getUserKeywords(userId = tempUserId).fold(
                onFailure = {
                    _uiState.emit(UiState.Failure)
                },
                onSuccess = { result ->
                    _uiState.updateSuccess {
                        it.copy(
                            keywords = result.toImmutableList(),
                        )
                    }
                },
            )
        }
    }
}
